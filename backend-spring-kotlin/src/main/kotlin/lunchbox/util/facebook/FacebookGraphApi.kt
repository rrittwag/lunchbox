@file:Suppress("ktlint:standard:discouraged-comment-location")

package lunchbox.util.facebook

import java.net.URL

/**
 * Führt ein Query via Facebook Graph API aus.
 * <p>
 * Das vorliegende Modell beschränkt sich auf die Resourcen Posts und Image der Graph API v2.10.
 */
interface FacebookGraphApi {
  fun <T : GraphApiResource> query(
    url: String,
    clazz: Class<T>,
  ): T?
}

inline fun <reified T : GraphApiResource> FacebookGraphApi.query(url: String): T? {
  return this.query(url, T::class.java)
}

// --- models ---

interface GraphApiResource

data class GraphApiData<T>(
  val data: List<T>,
)

// - posts -

data class Posts(
  val data: List<Post> = emptyList(),
) : GraphApiResource

data class Post(
  val message: String,
  val id: String,
  val attachments: GraphApiData<Attachment> = GraphApiData(emptyList()),
)

data class Attachment(
  val media: AttachmentMedia?, // ein Attachment enthält entweder media oder subattachments
  val subattachments: GraphApiData<Attachment> = GraphApiData(emptyList()),
  val target: AttachmentTarget,
  val url: URL,
)

data class AttachmentMedia(
  val image: AttachmentImage,
)

data class AttachmentImage(
  val height: Int,
  val src: URL,
  val width: Int,
)

data class AttachmentTarget(
  val id: String,
  val url: URL,
)

// - image -

data class Image(
  val id: String,
  val images: List<ImageVariant> = emptyList(),
) : GraphApiResource

data class ImageVariant(
  val height: Int,
  val width: Int,
  val source: URL,
)
