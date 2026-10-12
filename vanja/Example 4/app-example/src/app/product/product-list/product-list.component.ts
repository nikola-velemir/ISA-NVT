import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Toast } from 'bootstrap'
import { Product } from '../product';
import { ProductService } from '../service/product.service';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit {

  products: Product[] = [];
  currentPage: number = 1;
  itemsPerPage: number = 8;
  totalPages: number = 1;
  loading: boolean = false;

  toastHeader: string = '';
  toastText: string = '';

  private toastElement: ElementRef;
  @ViewChild('operationToast', { static: true }) set content(content: ElementRef) {
    this.toastElement = content;
    this.toast = new Toast(this.toastElement.nativeElement, {});
  }
  toast: any;

  constructor(private productService: ProductService) { }

  ngOnInit() {
    this.fetchData();
  }

  fetchData(): void {
    this.loading = true;
    this.productService.getPage(this.currentPage, this.itemsPerPage).then((data: any) => {
      this.products = data.data;
      this.totalPages = data.totalPages;
      this.loading = false;
    });
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.fetchData();
    }
  }

  previousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.fetchData();
    }
  }

  deleteProduct(id: number): void {
    this.loadingDeleteButton(id, true);
    this.productService.delete(id).then(() => {
      this.currentPage = 1;
      this.toastHeader = "Successful operation";
      this.toastText = "Product was successfully deleted.";
      this.showToast();
      this.loadingDeleteButton(id, false);
      this.fetchData();
    });
  }

  loadingDeleteButton(id: number, loading: boolean): void {
    const deleteButton = document.getElementById("delete" + "-" + id.toString());
    if (deleteButton) {
      if (loading) {
        deleteButton.classList.add("disabled");
        (deleteButton.firstChild as HTMLElement).style.display = "inline-block";
      } else {
        deleteButton.classList.remove("disabled");
        (deleteButton.firstChild as HTMLElement).style.display = "none";
      }
    }
  }

  addProduct(): void {
    this.loadingAddButton(true);
    this.productService.add().then(() => {
      this.toastHeader = "Successful operation";
      this.toastText = "Product was successfully created.";
      this.showToast();
      this.loadingAddButton(false);
      this.fetchData();
    });
  }

  loadingAddButton(loading: boolean): void {
    const addButton = document.getElementById("addProduct");
    if (addButton) {
      if (loading) {
        (addButton.firstChild as HTMLElement).style.display = "inline-block";
      } else {
        addButton.classList.remove("disabled");
        (addButton.firstChild as HTMLElement).style.display = "none";
      }
    }
  }

  showToast(): void {
    this.toast.show();
  }
}
