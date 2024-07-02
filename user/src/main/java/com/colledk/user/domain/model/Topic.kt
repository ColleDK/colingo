package com.colledk.user.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.colledk.user.R

enum class Topic(
    @StringRes val topicName: Int,
    @DrawableRes val icon: Int
) {
    ANIMALS(R.string.topic_animals, R.drawable.animal),
    COOKING(R.string.topic_cooking, R.drawable.cooking),
    THEATER(R.string.topic_theater, R.drawable.theater),
    PARTYING(R.string.topic_partying, R.drawable.party),
    BIKING(R.string.topic_biking, R.drawable.biking),
    CAMPING(R.string.topic_camping, R.drawable.camping),
    FITNESS(R.string.topic_fitness, R.drawable.fitness),
    MEDITATION(R.string.topic_meditation, R.drawable.meditation),
    MOVIES(R.string.topic_movie, R.drawable.movies),
    MUSIC(R.string.topic_music, R.drawable.music),
    PAINTING(R.string.topic_painting, R.drawable.painting),
    PHYSICS(R.string.topic_physics, R.drawable.physics),
    READING(R.string.topic_reading, R.drawable.reading),
    SCIENCE(R.string.topic_science, R.drawable.science),
    SPORTS(R.string.topic_sports, R.drawable.sports),
    SWIMMING(R.string.topic_swimming, R.drawable.swimming),
    TECHNOLOGY(R.string.topic_technology, R.drawable.technology),
    VIDEO_GAMES(R.string.topic_videogame, R.drawable.videogames)
}