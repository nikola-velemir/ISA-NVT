import { Injectable } from '@angular/core';
import { Product } from '../product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor() { }

  getAll(): Promise<Product[]> {
    return new Promise((resolve, _) => {
      setTimeout(() => {
        resolve(this.products)
      }, 10000);
    });
  }

  getPage(pageNumber: number, pageSize: number): Promise<any> {
    return new Promise((resolve, _) => {
      setTimeout(() => {
        const startIndex = (pageNumber - 1) * pageSize;
        let endIndex = startIndex + pageSize;
        if (endIndex > this.products.length) {
          endIndex = this.products.length;
        }
        const result = {
          data: this.products.slice(startIndex, endIndex),
          totalPages: Math.ceil(this.products.length / pageSize)
        }
        resolve(result);
      }, 2000);
    });
  }

  delete(id: number): Promise<void> {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        const productIndex = this.products.map(x => x.id).indexOf(id);
        if (productIndex > -1) {
          this.products.splice(productIndex, 1)
          resolve();
        } else {
          reject();
        }
      }, 1000);
    });
  }

  add(): Promise<Product> {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        const product: Product = {
          id: this.products.length === 0 ? 1 : this.products.length + 1,
          name: "new product",
          description: "new product description",
          price: 1000
        }
        this.products.push(product);
        resolve(product);
      }, 1000);
    });
  }

  products: Product[] = [
    { "id": 1, "name": "Laptop", "description": "A high-performance laptop", "price": 1200 },
    { "id": 2, "name": "Smartphone", "description": "Latest Android smartphone", "price": 800 },
    { "id": 3, "name": "Headphones", "description": "Noise-cancelling headphones", "price": 200 },
    { "id": 4, "name": "Keyboard", "description": "Mechanical keyboard with RGB lighting", "price": 150 },
    { "id": 5, "name": "Monitor", "description": "4K Ultra HD Monitor", "price": 300 },
    { "id": 6, "name": "Mouse", "description": "Wireless mouse", "price": 50 },
    { "id": 7, "name": "Tablet", "description": "Android Tablet", "price": 600 },
    { "id": 8, "name": "Smartwatch", "description": "Fitness tracking smartwatch", "price": 250 },
    { "id": 9, "name": "Printer", "description": "Color laser printer", "price": 400 },
    { "id": 10, "name": "Router", "description": "Wi-Fi 6 router", "price": 120 },
    { "id": 11, "name": "External Hard Drive", "description": "1TB portable hard drive", "price": 80 },
    { "id": 12, "name": "Flash Drive", "description": "64GB USB flash drive", "price": 15 },
    { "id": 13, "name": "Camera", "description": "Digital SLR camera", "price": 900 },
    { "id": 14, "name": "Action Camera", "description": "Waterproof action camera", "price": 350 },
    { "id": 15, "name": "VR Headset", "description": "Virtual reality headset", "price": 450 },
    { "id": 16, "name": "Gaming Console", "description": "Next-gen gaming console", "price": 500 },
    { "id": 17, "name": "Electric Kettle", "description": "1.7L electric kettle", "price": 40 },
    { "id": 18, "name": "Blender", "description": "High-speed blender", "price": 100 },
    { "id": 19, "name": "Coffee Maker", "description": "12-cup coffee maker", "price": 60 },
    { "id": 20, "name": "Vacuum Cleaner", "description": "Cordless vacuum cleaner", "price": 150 },
    { "id": 21, "name": "Air Fryer", "description": "4-quart air fryer", "price": 80 },
    { "id": 22, "name": "Hair Dryer", "description": "Professional hair dryer", "price": 70 },
    { "id": 23, "name": "Smart Light", "description": "Wi-Fi enabled smart bulb", "price": 25 },
    { "id": 24, "name": "Electric Toothbrush", "description": "Rechargeable electric toothbrush", "price": 40 },
    { "id": 25, "name": "Smart Thermostat", "description": "Wi-Fi programmable thermostat", "price": 220 },
    { "id": 26, "name": "Electric Shaver", "description": "Rechargeable electric shaver", "price": 80 },
    { "id": 27, "name": "Smart Speaker", "description": "Voice-controlled smart speaker", "price": 90 },
    { "id": 28, "name": "Smart Doorbell", "description": "Video doorbell with Wi-Fi", "price": 130 },
    { "id": 29, "name": "Smart Plug", "description": "Wi-Fi smart plug", "price": 25 },
    { "id": 30, "name": "Robot Vacuum", "description": "Automated robot vacuum cleaner", "price": 300 },
    { "id": 31, "name": "Smart Lock", "description": "Wi-Fi enabled door lock", "price": 180 },
    { "id": 32, "name": "Electric Scooter", "description": "Foldable electric scooter", "price": 400 },
    { "id": 33, "name": "Drone", "description": "Camera-equipped drone", "price": 800 },
    { "id": 34, "name": "Portable Projector", "description": "Compact portable projector", "price": 250 },
    { "id": 35, "name": "Electric Bike", "description": "Pedal-assist electric bicycle", "price": 1200 },
    { "id": 36, "name": "Smart Glasses", "description": "Augmented reality smart glasses", "price": 900 },
    { "id": 37, "name": "Smart Scale", "description": "Wi-Fi body composition scale", "price": 60 },
    { "id": 38, "name": "Smart Oven", "description": "Wi-Fi connected convection oven", "price": 350 },
    { "id": 39, "name": "Water Purifier", "description": "Home water purification system", "price": 120 },
    { "id": 40, "name": "Smart Fan", "description": "Remote-controlled ceiling fan", "price": 180 },
    { "id": 41, "name": "Dehumidifier", "description": "Compact dehumidifier for home", "price": 130 },
    { "id": 42, "name": "Air Purifier", "description": "HEPA air purifier", "price": 200 },
    { "id": 43, "name": "Portable AC", "description": "Portable air conditioner", "price": 500 },
    { "id": 44, "name": "Smart TV", "description": "4K Smart LED TV", "price": 700 },
    { "id": 45, "name": "Streaming Stick", "description": "HDMI streaming media player", "price": 50 },
    { "id": 46, "name": "Smart Watch", "description": "Health and fitness tracker", "price": 300 },
    { "id": 47, "name": "Gaming Laptop", "description": "High-end gaming laptop", "price": 1500 },
    { "id": 48, "name": "Graphics Card", "description": "High-performance graphics card", "price": 800 },
    { "id": 49, "name": "Smart Fridge", "description": "Wi-Fi enabled refrigerator", "price": 2500 },
    { "id": 50, "name": "Gaming Chair", "description": "Ergonomic gaming chair", "price": 200 }
  ];
}
