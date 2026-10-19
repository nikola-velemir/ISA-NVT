import { HttpClient, HttpResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable, tap } from 'rxjs';
import { Image } from '../models/image.model';

@Injectable({
  providedIn: 'root',
})
export class ImageService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/images';


  public download(keyName: string): Observable<Image.Raw.ForGet> {
    return this.http.get(`${this.apiUrl}/${keyName}`, {
      responseType: 'blob',
    })
      .pipe(
        map((response) => ({
          file: response,
          key: keyName
        }))
      )

  }
  public upload(request: Image.Raw.ForCreate): Observable<string> {
    const formData = new FormData();
    formData.append('file', request.file);

    return this.http
      .post<void>(this.apiUrl, formData, { observe: 'response' })
      .pipe(
        map((res) => {
          const location = res.headers.get('Location');
          if (!location) throw new Error('Missing Location header');
          return location;
        }),
      );
  }
  public getGetUrl(key: string): Observable<Image.URL.ForGet> {
    return this.http.get(`${this.apiUrl}/url/${key}`, {
      responseType: 'text',
    }).pipe(map((url) => (
      {
        key,
        url
      }
    ))
    )
  }

  public getPutUrl(): Observable<Image.URL.ForPut> {
    return this.http.put<Image.URL.ForPut>(`${this.apiUrl}/url`, {})
  }
}
