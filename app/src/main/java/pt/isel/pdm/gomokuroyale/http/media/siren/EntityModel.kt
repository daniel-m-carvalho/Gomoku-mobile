package pt.isel.pdm.gomokuroyale.http.media.siren

/**
 * Entity is a sub-entity that represents a resource.
 *
 * @property clazz is an array of strings that serves as an identifier for the link.
 * @property properties represent the properties of the entity.
 * @property links represent navigational links, distinct from entity relationships.
 * @property rel is the relationship of the link to its entity.
 * @property requireAuth is a boolean that indicates if the entity requires authentication.
 * */
data class EntityModel<T>(
    val clazz: List<String>,
    val properties: T,
    val links: List<LinkModel>,
    val rel: List<String>,
    val requireAuth: List<Boolean>
)