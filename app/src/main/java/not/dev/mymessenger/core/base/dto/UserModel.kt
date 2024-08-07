package not.dev.mymessenger.core.base.dto

import java.io.Serializable

data class UserModel(
    val userToken: String?=null,
    val firstname: String?=null,
    val email: String?=null,
    val name: String?=null,
):Serializable
