import {Injectable} from '@angular/core';

/**
 * Service for creating new browser window.
 */
@Injectable()
export class WindowService {
  constructor() {

  }

  createWindow(url: string, name: string = 'Oauth callback popup', width: number = window.outerWidth,
               height: number = window.outerHeight, left: number = width / 2, top: number = height / 2) {
    if (url == null) {
      return null;
    }

    width = Math.floor(window.outerWidth * 0.30);
    height = Math.floor(window.outerHeight * 0.65);

    if (name.includes('Facebook')) {
      width = Math.floor(window.outerWidth * 0.75);
    }

    if (window.outerWidth < 768) {
      width = 768
    }
    if (window.outerHeight < 640) {
      height = 640;
    }
    left = Math.floor(window.screenX + ((window.outerWidth - width) / 2));
    top = Math.floor(window.screenY + ((window.outerHeight - height) / 2));

    let options = `width=${width},height=${height},left=${left},top=${top}`;

    return window.open(url, name, options);
  }
}
