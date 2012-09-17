package fr.midipascher.persistence.impl;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import fr.midipascher.domain.Address;
import fr.midipascher.domain.FoodSpecialty;
import fr.midipascher.domain.Restaurant;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: louis
 * Date: 15/09/12
 * Time: 00:11
 * To change this template use File | Settings | File Templates.
 */
@Component(RestaurantToQueryBuilderConverter.BEAN_ID)
public class RestaurantToQueryBuilderConverter implements Converter<Restaurant, QueryBuilder> {

    public static final String BEAN_ID = "RestaurantToQueryBuilderConverter";

    public QueryBuilder convert(Restaurant source) {

      Map<String, Object> criteria = criteriaAsMap(source);

      QueryBuilder queryBuilder;

        if (noCriteria(criteria)) {

            queryBuilder = QueryBuilders.matchAllQuery();

        } else {

            queryBuilder = QueryBuilders.boolQuery();

            for (Map.Entry<String, Object> entry : criteria.entrySet()) {

                String field = entry.getKey();

                Object value = entry.getValue();

                QueryBuilder fieldQueryBuilder = resolveQueryBuilder(field, value);

                ((BoolQueryBuilder) queryBuilder).must(fieldQueryBuilder);

            }

        }

        return queryBuilder;

    }

    protected Map<String, Object> criteriaAsMap(Restaurant source) {

        if (source == null) return null;

        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();

        String name = source.getName();
        if (!Strings.isNullOrEmpty(name)) builder.put("name", name);

        String description = source.getDescription();
        if (!Strings.isNullOrEmpty(description)) builder.put("description", description);

        String mainOffer = source.getMainOffer();
        if (!Strings.isNullOrEmpty(mainOffer)) builder.put("mainOffer", mainOffer);

        Address address = source.getAddress();
        if (address != null) {

            String streetAddress = address.getStreetAddress();
            if (!Strings.isNullOrEmpty(streetAddress)) builder.put("address.streetAddress", streetAddress);

            String city = address.getCity();
            if (!Strings.isNullOrEmpty(city)) builder.put("address.city", city);

            String postalCode = address.getPostalCode();
            if (!Strings.isNullOrEmpty(postalCode)) builder.put("address.postalCode", postalCode);

            String countryCode = address.getCountryCode();
            if (!Strings.isNullOrEmpty(countryCode)) builder.put("address.countryCode", countryCode);

        }

        String companyId = source.getCompanyId();
        if (!Strings.isNullOrEmpty(companyId)) builder.put("companyId", companyId);

        Boolean halal = source.isHalal();
        if (halal != null) builder.put("halal", halal);

        Boolean kosher = source.isKosher();
        if (kosher != null) builder.put("kosher", kosher);

        Boolean vegetarian = source.isVegetarian();
        if (vegetarian != null) builder.put("vegetarian", vegetarian);

        Set<FoodSpecialty> specialties = source.getSpecialties();

        if (CollectionUtils.isNotEmpty(specialties)) {

            Collection<Long> ids = Collections2
                .transform(specialties, new Function<FoodSpecialty, Long>() {

                  public Long apply(FoodSpecialty foodSpecialty) {

                    return foodSpecialty == null || foodSpecialty.getId() == null ? null
                                                                                  : foodSpecialty
                                                                                      .getId();

                  }

                });

            builder.put("specialties", ids);

        }

        return builder.build();

    }

    protected QueryBuilder resolveQueryBuilder(String field, Object value) {

        if ("name".equals(field)
                || "description".equals(field)
                || "mainOffer".equals(field)
                || "address.streetAddress".equals(field)
                || "address.city".equals(field)
                || "address.postalCode".equals(field)) return QueryBuilders.queryString((String) value).field(field);

        if ("companyId".equals(field)
                || "kosher".equals(field)
                || "halal".equals(field)
                || "address.countryCode".equals(field)
                || "vegetarian".equals(field)) return QueryBuilders.termQuery(field, value);

        if ("specialties".equals(field)) return QueryBuilders.termsQuery(field + ".id", ((List<Long>) value).toArray());

        throw new UnsupportedOperationException("No query builder resolved for field '" + field + "'");

    }

    protected boolean noCriteria(Map<String, Object> criteria) {

        return criteria == null || criteria.size() == 0;

    }
}
