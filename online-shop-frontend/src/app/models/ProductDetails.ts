export class ProductDetails {
  id: number;
  category: string;
  name: string;
  description?: string;
  imageSrc: string;
  price: number;
  totalQuantity: number;
  sizeQuantityInfo?: any;
  addedQuantityToCart?: any;
  addedSizeToCart?: string;
  maximumQuantity? :number;
  wishlisted: boolean;
}
