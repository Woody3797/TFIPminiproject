export interface ChatMessage {
    chatID: string
    sender: string
    recipient: string
    content: string
    productID: number
    timestamp: Date
}

export interface ChatConvo {
    chatID: string
    productID: number
    messages: ChatMessage[]
}