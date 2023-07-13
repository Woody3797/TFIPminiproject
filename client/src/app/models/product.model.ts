export interface Product {
    productID: number
    productName: string
    description: string
    price: number
    email: string
    uploadTime: string
    images: Image[]
    productStatus: string
}

export interface Image {
    imageID: number
    imageName: string
    type: string
    imageBytes: any
    url: string
}

export interface UploadProduct {
    productName: string
    price: number
    description: string
    email: string
}

export interface OrderDetails {
    id: string
    productID: number
    seller: string
    buyers: string[]
    status: string
}

export interface ProductTags {
    productID: number
    tags: string[]
    status: string
}

export interface LikedProducts {
    email: string
    productIDs: number[]
}