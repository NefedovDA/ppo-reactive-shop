package ru.diamant.rabbit.reactiveShop.domain

data class FullName(
    val firstName: String,
    val lastName: String,
    val authorName: String
) {
    init {
        require(firstName.contains(',').not())
        require(lastName.contains(',').not())
        require(authorName.contains(',').not())
    }

    override fun toString(): String =
        "($firstName,$lastName,$authorName)"

    companion object {
        operator fun invoke(dbValue: String): FullName {
            val (firstName, lastName, authorName) =
                dbValue
                    .trimStart('(').trimEnd(')')
                    .split(',')
                    .also { require(it.size == 3) }

            return FullName(firstName, lastName, authorName)
        }
    }
}