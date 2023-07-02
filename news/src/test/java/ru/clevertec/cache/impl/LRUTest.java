package ru.clevertec.cache.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.cache.CacheAlgorithm;
import ru.clevertec.data.NewsTestBuilder;
import ru.clevertec.repository.entity.News;

import static org.assertj.core.api.Assertions.assertThat;

class LRUTest {

    private CacheAlgorithm<Long, News> cache;
    private final Long firstId = 1L;
    private final Long secondId = 2L;
    private final Long thirdId = 3L;
    private final News actual1 = NewsTestBuilder.aNews().withId(firstId).build();
    private final News actual2 = NewsTestBuilder.aNews().withId(secondId).build();
    private final News actual3 = NewsTestBuilder.aNews().withId(thirdId).build();

    @BeforeEach
    void setUp() {
        this.cache = new LRU<>(3);

        cache.post(actual1.getId(), actual1);
        cache.post(actual2.getId(), actual2);
        cache.post(actual3.getId(), actual3);
    }

    @Test
    void getNewsWitchShouldBeInCache() {
        News expected1 = cache.get(firstId);
        News expected2 = cache.get(secondId);
        News expected3 = cache.get(thirdId);

        assertThat(expected1).isEqualTo(actual1);
        assertThat(expected2).isEqualTo(actual2);
        assertThat(expected3).isEqualTo(actual3);
    }

    @Test
    void getShouldReturnNullBecauseEntityDontInsert() {
        News expected1 = cache.get(4L);
        News expected2 = cache.get(5L);
        News expected3 = cache.get(6L);

        assertThat(expected1).isNull();
        assertThat(expected2).isNull();
        assertThat(expected3).isNull();
    }

    @Test
    void postSuccessfully() {
        News expected1 = cache.get(firstId);
        News expected2 = cache.get(secondId);
        News expected3 = cache.get(thirdId);

        assertThat(expected1).isNotNull();
        assertThat(expected2).isNotNull();
        assertThat(expected3).isNotNull();
    }

    @Test
    void postShouldRemoveEldest() {
        News actual1 = NewsTestBuilder.aNews().withId(4L).build();
        News actual2 = NewsTestBuilder.aNews().withId(5L).build();
        News actual3 = NewsTestBuilder.aNews().withId(6L).build();

        cache.post(actual1.getId(), actual1);
        cache.post(actual2.getId(), actual2);
        cache.post(actual3.getId(), actual3);

        News expected1 = cache.get(firstId);
        News expected2 = cache.get(secondId);
        News expected3 = cache.get(thirdId);

        assertThat(expected1).isNull();
        assertThat(expected2).isNull();
        assertThat(expected3).isNull();
    }

    @Test
    void delete() {
        cache.delete(firstId);

        News actual = cache.get(firstId);

        assertThat(actual).isNull();
    }

    @Test
    void putSuccessfully() {
        String text = "Some text to change news";
        actual1.setText(text);

        cache.put(actual1.getId(), actual1);

        News newsWithChange = cache.get(actual1.getId());

        assertThat(newsWithChange.getText()).isEqualTo(text);
    }
}