export interface ChatMessage {
    chatID: string
    productID: number
    sender: string
    recipient: string
    content: string
    timestamp?: Date | undefined
}