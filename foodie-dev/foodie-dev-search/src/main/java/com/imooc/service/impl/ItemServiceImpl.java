package com.imooc.service.impl;

import com.imooc.es.pojo.Items;
import com.imooc.es.pojo.Stu;
import com.imooc.service.ItemService;
import com.imooc.utils.PagedGridResult;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ElasticsearchTemplate esTemplate;

    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        String preTag = "<font color='red'>";
        String postTag = "</font>";

        Pageable pageable = PageRequest.of(page, pageSize);

//        SortBuilder sortBuilder = new FieldSortBuilder("money")
//                .order(SortOrder.DESC);
//        SortBuilder sortBuilderAge = new FieldSortBuilder("age")
//                .order(SortOrder.ASC);

        String itemNameFiled = "itemName";
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery(itemNameFiled, keywords))
                .withHighlightFields(new HighlightBuilder.Field(itemNameFiled)
                        .preTags(preTag)
                        .postTags(postTag))
//                .withSort(sortBuilder)
//                .withSort(sortBuilderAge)
                .withPageable(pageable)
                .build();
        AggregatedPage<Items> pagedItem = esTemplate.queryForPage(query, Items.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {

                List<Items> itemsListHighlight = new ArrayList<>();

                SearchHits hits = response.getHits();
                for (SearchHit h : hits) {
                    HighlightField highlightField = h.getHighlightFields().get(itemNameFiled);
                    String itemName = highlightField.getFragments()[0].toString();

                    String itemId = (String)h.getSourceAsMap().get("itemId");
                    String imgUrl = (String)h.getSourceAsMap().get("imgUrl");
                    Integer price = (Integer)h.getSourceAsMap().get("price");
                    Integer sellCounts = (Integer)h.getSourceAsMap().get("sellCounts");

                    Items itemsHL = new Items();
                    itemsHL.setItemId(itemId);
                    itemsHL.setItemName(itemName);
                    itemsHL.setImgUrl(imgUrl);
                    itemsHL.setPrice(price);
                    itemsHL.setSellCounts(sellCounts);

                    itemsListHighlight.add(itemsHL);
                }

                return new AggregatedPageImpl<>((List<T>)itemsListHighlight, pageable, hits.totalHits);

            }
        });

        PagedGridResult pagedGridResult = new PagedGridResult();
        pagedGridResult.setRows(pagedItem.getContent());
        pagedGridResult.setPage(page+1);
        pagedGridResult.setTotal(pagedItem.getTotalPages());
        pagedGridResult.setRecords(pagedItem.getTotalElements());

        return pagedGridResult;

    }
}
