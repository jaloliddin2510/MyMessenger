package not.dev.mymessenger.core.base.dto

data class ChatModel(
    val message: String?=null,
    val myToken:String?=null,
    val friendToken:String?=null,
) {

}
//    val sendTime: Long = System.currentTimeMillis()

//val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
//val formattedTime = dateFormat.format(Date(sendTime))