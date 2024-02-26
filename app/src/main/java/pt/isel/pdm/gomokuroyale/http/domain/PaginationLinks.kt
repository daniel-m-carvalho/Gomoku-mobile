package pt.isel.pdm.gomokuroyale.http.domain

data class PaginationLinks(
    val first: String?,
    val prev: String?,
    val next: String?,
    val last: String?
) {
    companion object {
        private fun extractLink(links: Map<String, String>, key: String) =
            links[key]?.split("=")?.get(1)

        fun from(links: Map<String, String>): PaginationLinks {
            return PaginationLinks(
                first = extractLink(links, "first"),
                prev = extractLink(links, "prev"),
                next = extractLink(links, "next"),
                last = extractLink(links, "last")
            )
        }
    }
}
