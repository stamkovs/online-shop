import {Component, ElementRef, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {StorageService} from '../../../services/storage.service';
import {catchError, takeUntil} from 'rxjs/operators';
import {Subject} from 'rxjs';
import {SpinnerService} from '../../../services/spinner.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {ProductDetails} from '../../../models/ProductDetails';
import {ProductService} from '../../../services/product.service';
import {Router} from '@angular/router';

interface Category {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-product-add',
  templateUrl: './product-add.component.html',
  styleUrls: ['./product-add.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ProductAddComponent implements OnInit {

  name: string = '';
  category: string = '';
  price: string = '';
  description: string;
  srcResult: string | ArrayBuffer;
  imageName: string = '';
  disabled: boolean = true;
  destroy$: Subject<null> = new Subject();
  fileToUpload: File;
  quantity: number;
  size: number;

  categories: Category[] = [
    {value: 'men_sneakers', viewValue: 'Men sneakers'},
    {value: 'women_sneakers', viewValue: 'Women sneakers'},
    {value: 'sports', viewValue: 'Sports'},
    {value: 'electronics', viewValue: 'Electronics'},
    {value: 'watches', viewValue: 'Watches'},
    {value: 'jewelry', viewValue: 'Jewelry'},
  ];
  sizeFormFields: any[];
  sizeInputs: any;
  quantityInputs: any;
  sizes: number[];
  quantities: number[];
  sizeQuantityInputsMap: any;

  @ViewChild('fileInput')
  fileUploadInput: ElementRef;

  submitted: boolean = false;
  submitBtnLabel: string;

  constructor(private readonly storageService: StorageService, private spinnerService: SpinnerService,
              private productService: ProductService, private _snackBar: MatSnackBar, private router: Router) {
  }

  ngOnInit(): void {
    this.submitBtnLabel = 'Upload product';
    this.sizeFormFields = [0];
    this.sizes = [];
    this.quantities = [];
  }

  nameModelChange(event) {
    this.name = event;
    this.checkIfAllFieldsAreValid();
  }

  categoryModelChange(event) {
    this.category = event.value;
    this.checkIfAllFieldsAreValid();
  }

  priceModelChange(event) {
    this.price = event;
    this.checkIfAllFieldsAreValid();
  }

  descriptionModelChange(event) {
    this.description = event;
    this.checkIfAllFieldsAreValid();
  }

  onFileSelected(event) {
    if (event.target.files && event.target.files[0]) {
      var reader = new FileReader();

      reader.readAsDataURL(event.target.files[0]); // read file as data url
      this.imageName = event.target.files[0].name;
      this.fileToUpload = event.target.files[0];
      reader.onload = (event) => { // called once readAsDataURL is completed
        this.srcResult = event.target.result;
      }
    }
    this.checkIfAllFieldsAreValid();
  }

  removeSelectedPhoto() {
    this.imageName = '';
    this.srcResult = '';
    this.fileUploadInput.nativeElement.value = "";
    this.checkIfAllFieldsAreValid();
  }

  checkIfAllFieldsAreValid() {
    this.submitBtnLabel = 'Upload product';
    this.disabled = this.name == '' || this.description == '' || this.category == '' || this.price == ''
      || this.imageName == '';
  }

  addNewProductSizeAndQuantityFormFields() {
    if (this.sizeFormFields.length === 10) {
      this.openSnackBar('Maximum amount of size fields added. Range is from 36-45');
      return;
    }
    this.sizeFormFields.push(this.sizeFormFields.length);
  }

  removeLastProductSizeAndQuantityFormField() {
    if (this.sizeFormFields.length === 1) {
      this.openSnackBar('There must be at least one size field added. Range is from 36-45');
      return;
    }
    this.sizeFormFields.pop();
    this.sizes.pop();
    this.quantities.pop();
  }

  getSizes() {
    this.sizeInputs = document.getElementsByClassName('product-size-input');
    this.quantityInputs = document.getElementsByClassName('product-quantity-per-size-input');
    this.sizes = [];
    for (let i = 0; i < this.sizeInputs.length; i++) {
      this.sizes.push(this.sizeInputs[i].value);
    }

    this.quantities = [];
    for (let i = 0; i < this.quantityInputs.length; i++) {
      this.quantities.push(this.quantityInputs[i].value);
    }

    // map them
    this.sizeQuantityInputsMap = {};
    this.sizeQuantityInputsMap.size = {};
    for (let i = 0; i < this.sizeInputs.length; i++) {
      if (this.quantities[i] && this.sizes[i]) {
        this.sizeQuantityInputsMap.size[this.sizes[i]] = this.quantities[i];
      }
    }
    return this.sizeQuantityInputsMap;
  }

  uploadProduct() {
    this.getSizes();
    const sizeQuantityInputsMapKeys = Object.keys(this.sizeQuantityInputsMap.size);
    if (sizeQuantityInputsMapKeys.length === 0) {
      this.openSnackBar('Size and quantity must be provided at least once');
      return;
    }

    this.spinnerService.show();
    this.submitted = true;
    let mediaFolderPath;
    switch (this.category) {
      case 'men_sneakers':
        mediaFolderPath = 'products/images/men-sneakers';
        break;
      case 'watches':
        mediaFolderPath = 'products/images/watches';
        break;
      default:
        mediaFolderPath = 'products/images';
    }

    const {downloadUrl} = this.storageService.uploadFileAndGetMetadata(
      mediaFolderPath,
      this.fileToUpload,
      this.name
    );
    downloadUrl
      .pipe(
        takeUntil(this.destroy$),
        catchError(() => {
          this.router.navigate(['/home']);
          return 'error';
        }),
      )
      .subscribe((downloadUrl) => {
        const productDetails = new ProductDetails();
        productDetails.name = this.name;
        productDetails.category = this.category;
        productDetails.price = +this.price;
        productDetails.sizeQuantityInfo = this.sizeQuantityInputsMap;
        productDetails.description = this.description;
        productDetails.imageSrc = downloadUrl;
        this.uploadProductDetails(productDetails);
        this.submitBtnLabel = 'Upload done!';
      });
  }

  uploadProductDetails(productDetails: ProductDetails) {
    this.productService.addProduct(productDetails).subscribe((data: any) => {
      this.router.navigate(['/products', data.category, data.id]);
    })
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, 'Close', {
      duration: 5000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
    });
  }

}
