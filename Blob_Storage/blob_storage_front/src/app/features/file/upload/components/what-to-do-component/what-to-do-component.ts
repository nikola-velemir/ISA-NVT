import { HttpClient } from '@angular/common/http';
import { Component, DestroyRef, inject, signal } from '@angular/core';
import { map, Subscription, switchMap } from 'rxjs';
import { ImageService } from '../../../../../core/services/image.service';

interface Result {
  name: string;
  url: string;
}

@Component({
  selector: 'app-what-to-do',
  templateUrl: './what-to-do-component.html',
  standalone: true
})
export class WhatToDoComponent {
  private readonly http = inject(HttpClient);
  private readonly service = inject(ImageService);
  private sub?: Subscription;

  readonly dragging = signal(false);
  readonly uploading = signal(false);
  readonly error = signal('');
  readonly result = signal<Result | null>(null);

  constructor() {
    inject(DestroyRef).onDestroy(() => this.sub?.unsubscribe());
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.handleFile(input.files?.[0]);
    input.value = ''; // allow re-selecting the same file
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    this.dragging.set(true);
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    this.dragging.set(false);
    this.handleFile(event.dataTransfer?.files?.[0]);
  }

  clear(): void {
    this.sub?.unsubscribe();
    this.result.set(null);
    this.uploading.set(false);
    this.error.set('');
  }

  private handleFile(file?: File | null): void {
    if (!file) return;

    if (!file.type.startsWith('image/')) {
      this.error.set('Only image files are supported.');
      return;
    }

    this.error.set('');
    this.send(file);
  }

  private send(file: File): void {
    // A new file replaces any upload still in progress
    this.sub?.unsubscribe();
    this.uploading.set(true);

    this.sub = this.service
      .getPutUrl() // 1. ask the backend for a presigned PUT url
      .pipe(
        // 2. send the file straight to storage
        switchMap((put) =>
          this.http
            .put(put.url, file, { headers: { 'Content-Type': file.type } })
            .pipe(map(() => put.key)),
        ),
        // 3. ask the backend for a presigned GET url for the stored file
        switchMap((key) => this.service.getGetUrl(key)),
      )
      .subscribe({
        next: (res) => {
          // Nothing is shown until the whole chain has succeeded
          this.result.set({ name: file.name, url: res.url });
          this.uploading.set(false);
        },
        error: () => {
          // Keep whatever was last presented
          this.uploading.set(false);
          this.error.set('Upload failed. Try again.');
        },
      });
  }
  onImageError(): void {
    this.error.set('The image could not be loaded.');
  }
}