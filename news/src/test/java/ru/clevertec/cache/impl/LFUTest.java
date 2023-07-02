package ru.clevertec.cache.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.cache.CacheAlgorithm;
import ru.clevertec.data.NewsTestBuilder;
import ru.clevertec.repository.entity.News;

import static org.assertj.core.api.Assertions.assertThat;

class LFUTest {

    private CacheAlgorithm<Long, News> cache;
    private final Long firstId = 1L;
    private final Long secondId = 2L;
    private final Long thirdId = 3L;
    private final News actual1 = NewsTestBuilder.aNews().withId(firstId).build();
    private final News actual2 = NewsTestBuilder.aNews().withId(secondId).build();
    private final News actual3 = NewsTestBuilder.aNews().withId(thirdId).build();

    @BeforeEach
    void setUp() {
        this.cache = new LFU<>(3);

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
    void postShouldRemoveMostRarelyUsed() {
        News insert1 = NewsTestBuilder.aNews().withId(4L).build();
        News insert2 = NewsTestBuilder.aNews().withId(5L).build();
        News insert3 = NewsTestBuilder.aNews().withId(6L).build();

        cache.get(firstId);
        cache.get(secondId);

        cache.post(insert1.getId(), insert1);
        cache.post(insert2.getId(), insert2);
        cache.post(insert3.getId(), insert3);

        News expected1 = cache.get(firstId);
        News expected2 = cache.get(secondId);
        News expected3 = cache.get(thirdId);
        News expected4 = cache.get(insert1.getId());
        News expected5 = cache.get(insert2.getId());
        News expected6 = cache.get(insert3.getId());

        assertThat(expected1).isEqualTo(actual1);
        assertThat(expected2).isEqualTo(actual2);
        assertThat(expected3).isNull();
        assertThat(expected4).isNull();
        assertThat(expected5).isNull();
        assertThat(expected6).isEqualTo(insert3);

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