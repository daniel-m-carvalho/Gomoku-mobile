package pt.isel.pdm.gomokuroyale.http.media.siren

/**
 * Field is one of the input fields of the action.
 *
 * @property name is a string that identifies the field to be set.
 * @property type is the media type of the field.
 * @property value is the value of the field.
 * */

data class FieldModel(
    val name: String,
    val type: String,
    val value: String? = null,
)