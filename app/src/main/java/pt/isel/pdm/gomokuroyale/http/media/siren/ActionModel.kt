package pt.isel.pdm.gomokuroyale.http.media.siren

/**
 * Action is a set of instructions that can be carried out by the client.
 *
 * @property name is a string that identifies the action to be performed.
 * @property method is a string that identifies the protocol method to use.
 * @property href is the URI of the action.
 * @property type is the media type of the action.
 * @property fields represent the input fields of the action.
 * @property requireAuth is a boolean that indicates if the action requires authentication.
 * */

data class ActionModel(
    val name: String,
    val href: String,
    val method: String,
    val type: String,
    val fields: List<FieldModel>,
    val requireAuth: List<Boolean>
)