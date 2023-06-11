export interface Product {
    productID: number
    productName: string
    description: string
    price: number
    username: string
    uploadTime: string
    images: Image[]
}

export interface Image {
    imageID: number
    imageName: string
    type: string
    imageBytes: string
    url: string
}


export interface UploadProduct {
    productName: string
    description: string
    price: number
}