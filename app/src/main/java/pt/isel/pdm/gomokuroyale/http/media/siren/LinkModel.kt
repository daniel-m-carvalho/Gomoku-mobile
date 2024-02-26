package pt.isel.pdm.gomokuroyale.http.media.siren

/**
 * Link is a navigational link, distinct from entity relationships.
 *
 * @property rel is the relationship of the link to its entity.
 * @property href is the URI of the linked resource.
 * */
data class LinkModel(
    val rel: List<String>,
    val href: String,
)