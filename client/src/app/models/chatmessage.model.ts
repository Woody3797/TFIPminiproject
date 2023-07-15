export interface ChatMessage {
    messageID: string
    chatID: string
    sender: string
    recipient: string
    content: string
    timestamp: Date
}