package lunchbox.util.container

import org.testcontainers.containers.GenericContainer

class KtGenericContainer(
  image: String,
) : GenericContainer<KtGenericContainer>(image)
