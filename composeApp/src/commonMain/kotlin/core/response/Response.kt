package core.response

interface Response {
    val success: Boolean?
    val data: Any?
    val message: String?
}