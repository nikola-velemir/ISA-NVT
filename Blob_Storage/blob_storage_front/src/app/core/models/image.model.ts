export interface Image {
    key: string;
}

export namespace Image {
    export namespace Raw {
        export type ForGet = Image & {
            file: Blob;
        }
        export type ForCreate = {
            file: Blob
        };
    }
    export namespace URL {
        export type ForGet = {url:string};
        export type ForPut = Image & {
            url: string;
        };
    }
}