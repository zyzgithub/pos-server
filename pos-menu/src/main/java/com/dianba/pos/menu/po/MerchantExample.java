package com.dianba.pos.menu.po;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MerchantExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public MerchantExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andTitleIsNull() {
            addCriterion("title is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("title is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("title =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("title <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("title >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("title >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("title <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("title <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("title like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("title not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("title in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("title not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("title between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("title not between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andGroupIdIsNull() {
            addCriterion("group_id is null");
            return (Criteria) this;
        }

        public Criteria andGroupIdIsNotNull() {
            addCriterion("group_id is not null");
            return (Criteria) this;
        }

        public Criteria andGroupIdEqualTo(Long value) {
            addCriterion("group_id =", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdNotEqualTo(Long value) {
            addCriterion("group_id <>", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdGreaterThan(Long value) {
            addCriterion("group_id >", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdGreaterThanOrEqualTo(Long value) {
            addCriterion("group_id >=", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdLessThan(Long value) {
            addCriterion("group_id <", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdLessThanOrEqualTo(Long value) {
            addCriterion("group_id <=", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdIn(List<Long> values) {
            addCriterion("group_id in", values, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdNotIn(List<Long> values) {
            addCriterion("group_id not in", values, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdBetween(Long value1, Long value2) {
            addCriterion("group_id between", value1, value2, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdNotBetween(Long value1, Long value2) {
            addCriterion("group_id not between", value1, value2, "groupId");
            return (Criteria) this;
        }

        public Criteria andCityIdIsNull() {
            addCriterion("city_id is null");
            return (Criteria) this;
        }

        public Criteria andCityIdIsNotNull() {
            addCriterion("city_id is not null");
            return (Criteria) this;
        }

        public Criteria andCityIdEqualTo(Long value) {
            addCriterion("city_id =", value, "cityId");
            return (Criteria) this;
        }

        public Criteria andCityIdNotEqualTo(Long value) {
            addCriterion("city_id <>", value, "cityId");
            return (Criteria) this;
        }

        public Criteria andCityIdGreaterThan(Long value) {
            addCriterion("city_id >", value, "cityId");
            return (Criteria) this;
        }

        public Criteria andCityIdGreaterThanOrEqualTo(Long value) {
            addCriterion("city_id >=", value, "cityId");
            return (Criteria) this;
        }

        public Criteria andCityIdLessThan(Long value) {
            addCriterion("city_id <", value, "cityId");
            return (Criteria) this;
        }

        public Criteria andCityIdLessThanOrEqualTo(Long value) {
            addCriterion("city_id <=", value, "cityId");
            return (Criteria) this;
        }

        public Criteria andCityIdIn(List<Long> values) {
            addCriterion("city_id in", values, "cityId");
            return (Criteria) this;
        }

        public Criteria andCityIdNotIn(List<Long> values) {
            addCriterion("city_id not in", values, "cityId");
            return (Criteria) this;
        }

        public Criteria andCityIdBetween(Long value1, Long value2) {
            addCriterion("city_id between", value1, value2, "cityId");
            return (Criteria) this;
        }

        public Criteria andCityIdNotBetween(Long value1, Long value2) {
            addCriterion("city_id not between", value1, value2, "cityId");
            return (Criteria) this;
        }

        public Criteria andBankNameIsNull() {
            addCriterion("bank_name is null");
            return (Criteria) this;
        }

        public Criteria andBankNameIsNotNull() {
            addCriterion("bank_name is not null");
            return (Criteria) this;
        }

        public Criteria andBankNameEqualTo(String value) {
            addCriterion("bank_name =", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotEqualTo(String value) {
            addCriterion("bank_name <>", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameGreaterThan(String value) {
            addCriterion("bank_name >", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameGreaterThanOrEqualTo(String value) {
            addCriterion("bank_name >=", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameLessThan(String value) {
            addCriterion("bank_name <", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameLessThanOrEqualTo(String value) {
            addCriterion("bank_name <=", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameLike(String value) {
            addCriterion("bank_name like", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotLike(String value) {
            addCriterion("bank_name not like", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameIn(List<String> values) {
            addCriterion("bank_name in", values, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotIn(List<String> values) {
            addCriterion("bank_name not in", values, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameBetween(String value1, String value2) {
            addCriterion("bank_name between", value1, value2, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotBetween(String value1, String value2) {
            addCriterion("bank_name not between", value1, value2, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNoIsNull() {
            addCriterion("bank_no is null");
            return (Criteria) this;
        }

        public Criteria andBankNoIsNotNull() {
            addCriterion("bank_no is not null");
            return (Criteria) this;
        }

        public Criteria andBankNoEqualTo(String value) {
            addCriterion("bank_no =", value, "bankNo");
            return (Criteria) this;
        }

        public Criteria andBankNoNotEqualTo(String value) {
            addCriterion("bank_no <>", value, "bankNo");
            return (Criteria) this;
        }

        public Criteria andBankNoGreaterThan(String value) {
            addCriterion("bank_no >", value, "bankNo");
            return (Criteria) this;
        }

        public Criteria andBankNoGreaterThanOrEqualTo(String value) {
            addCriterion("bank_no >=", value, "bankNo");
            return (Criteria) this;
        }

        public Criteria andBankNoLessThan(String value) {
            addCriterion("bank_no <", value, "bankNo");
            return (Criteria) this;
        }

        public Criteria andBankNoLessThanOrEqualTo(String value) {
            addCriterion("bank_no <=", value, "bankNo");
            return (Criteria) this;
        }

        public Criteria andBankNoLike(String value) {
            addCriterion("bank_no like", value, "bankNo");
            return (Criteria) this;
        }

        public Criteria andBankNoNotLike(String value) {
            addCriterion("bank_no not like", value, "bankNo");
            return (Criteria) this;
        }

        public Criteria andBankNoIn(List<String> values) {
            addCriterion("bank_no in", values, "bankNo");
            return (Criteria) this;
        }

        public Criteria andBankNoNotIn(List<String> values) {
            addCriterion("bank_no not in", values, "bankNo");
            return (Criteria) this;
        }

        public Criteria andBankNoBetween(String value1, String value2) {
            addCriterion("bank_no between", value1, value2, "bankNo");
            return (Criteria) this;
        }

        public Criteria andBankNoNotBetween(String value1, String value2) {
            addCriterion("bank_no not between", value1, value2, "bankNo");
            return (Criteria) this;
        }

        public Criteria andBankUserIsNull() {
            addCriterion("bank_user is null");
            return (Criteria) this;
        }

        public Criteria andBankUserIsNotNull() {
            addCriterion("bank_user is not null");
            return (Criteria) this;
        }

        public Criteria andBankUserEqualTo(String value) {
            addCriterion("bank_user =", value, "bankUser");
            return (Criteria) this;
        }

        public Criteria andBankUserNotEqualTo(String value) {
            addCriterion("bank_user <>", value, "bankUser");
            return (Criteria) this;
        }

        public Criteria andBankUserGreaterThan(String value) {
            addCriterion("bank_user >", value, "bankUser");
            return (Criteria) this;
        }

        public Criteria andBankUserGreaterThanOrEqualTo(String value) {
            addCriterion("bank_user >=", value, "bankUser");
            return (Criteria) this;
        }

        public Criteria andBankUserLessThan(String value) {
            addCriterion("bank_user <", value, "bankUser");
            return (Criteria) this;
        }

        public Criteria andBankUserLessThanOrEqualTo(String value) {
            addCriterion("bank_user <=", value, "bankUser");
            return (Criteria) this;
        }

        public Criteria andBankUserLike(String value) {
            addCriterion("bank_user like", value, "bankUser");
            return (Criteria) this;
        }

        public Criteria andBankUserNotLike(String value) {
            addCriterion("bank_user not like", value, "bankUser");
            return (Criteria) this;
        }

        public Criteria andBankUserIn(List<String> values) {
            addCriterion("bank_user in", values, "bankUser");
            return (Criteria) this;
        }

        public Criteria andBankUserNotIn(List<String> values) {
            addCriterion("bank_user not in", values, "bankUser");
            return (Criteria) this;
        }

        public Criteria andBankUserBetween(String value1, String value2) {
            addCriterion("bank_user between", value1, value2, "bankUser");
            return (Criteria) this;
        }

        public Criteria andBankUserNotBetween(String value1, String value2) {
            addCriterion("bank_user not between", value1, value2, "bankUser");
            return (Criteria) this;
        }

        public Criteria andAddressIsNull() {
            addCriterion("address is null");
            return (Criteria) this;
        }

        public Criteria andAddressIsNotNull() {
            addCriterion("address is not null");
            return (Criteria) this;
        }

        public Criteria andAddressEqualTo(String value) {
            addCriterion("address =", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotEqualTo(String value) {
            addCriterion("address <>", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThan(String value) {
            addCriterion("address >", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThanOrEqualTo(String value) {
            addCriterion("address >=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThan(String value) {
            addCriterion("address <", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThanOrEqualTo(String value) {
            addCriterion("address <=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLike(String value) {
            addCriterion("address like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotLike(String value) {
            addCriterion("address not like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressIn(List<String> values) {
            addCriterion("address in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotIn(List<String> values) {
            addCriterion("address not in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressBetween(String value1, String value2) {
            addCriterion("address between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotBetween(String value1, String value2) {
            addCriterion("address not between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andContactIsNull() {
            addCriterion("contact is null");
            return (Criteria) this;
        }

        public Criteria andContactIsNotNull() {
            addCriterion("contact is not null");
            return (Criteria) this;
        }

        public Criteria andContactEqualTo(String value) {
            addCriterion("contact =", value, "contact");
            return (Criteria) this;
        }

        public Criteria andContactNotEqualTo(String value) {
            addCriterion("contact <>", value, "contact");
            return (Criteria) this;
        }

        public Criteria andContactGreaterThan(String value) {
            addCriterion("contact >", value, "contact");
            return (Criteria) this;
        }

        public Criteria andContactGreaterThanOrEqualTo(String value) {
            addCriterion("contact >=", value, "contact");
            return (Criteria) this;
        }

        public Criteria andContactLessThan(String value) {
            addCriterion("contact <", value, "contact");
            return (Criteria) this;
        }

        public Criteria andContactLessThanOrEqualTo(String value) {
            addCriterion("contact <=", value, "contact");
            return (Criteria) this;
        }

        public Criteria andContactLike(String value) {
            addCriterion("contact like", value, "contact");
            return (Criteria) this;
        }

        public Criteria andContactNotLike(String value) {
            addCriterion("contact not like", value, "contact");
            return (Criteria) this;
        }

        public Criteria andContactIn(List<String> values) {
            addCriterion("contact in", values, "contact");
            return (Criteria) this;
        }

        public Criteria andContactNotIn(List<String> values) {
            addCriterion("contact not in", values, "contact");
            return (Criteria) this;
        }

        public Criteria andContactBetween(String value1, String value2) {
            addCriterion("contact between", value1, value2, "contact");
            return (Criteria) this;
        }

        public Criteria andContactNotBetween(String value1, String value2) {
            addCriterion("contact not between", value1, value2, "contact");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNull() {
            addCriterion("phone is null");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNotNull() {
            addCriterion("phone is not null");
            return (Criteria) this;
        }

        public Criteria andPhoneEqualTo(String value) {
            addCriterion("phone =", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotEqualTo(String value) {
            addCriterion("phone <>", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThan(String value) {
            addCriterion("phone >", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("phone >=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThan(String value) {
            addCriterion("phone <", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThanOrEqualTo(String value) {
            addCriterion("phone <=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLike(String value) {
            addCriterion("phone like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotLike(String value) {
            addCriterion("phone not like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneIn(List<String> values) {
            addCriterion("phone in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotIn(List<String> values) {
            addCriterion("phone not in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneBetween(String value1, String value2) {
            addCriterion("phone between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotBetween(String value1, String value2) {
            addCriterion("phone not between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andLocationIsNull() {
            addCriterion("location is null");
            return (Criteria) this;
        }

        public Criteria andLocationIsNotNull() {
            addCriterion("location is not null");
            return (Criteria) this;
        }

        public Criteria andLocationEqualTo(String value) {
            addCriterion("location =", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationNotEqualTo(String value) {
            addCriterion("location <>", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationGreaterThan(String value) {
            addCriterion("location >", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationGreaterThanOrEqualTo(String value) {
            addCriterion("location >=", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationLessThan(String value) {
            addCriterion("location <", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationLessThanOrEqualTo(String value) {
            addCriterion("location <=", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationLike(String value) {
            addCriterion("location like", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationNotLike(String value) {
            addCriterion("location not like", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationIn(List<String> values) {
            addCriterion("location in", values, "location");
            return (Criteria) this;
        }

        public Criteria andLocationNotIn(List<String> values) {
            addCriterion("location not in", values, "location");
            return (Criteria) this;
        }

        public Criteria andLocationBetween(String value1, String value2) {
            addCriterion("location between", value1, value2, "location");
            return (Criteria) this;
        }

        public Criteria andLocationNotBetween(String value1, String value2) {
            addCriterion("location not between", value1, value2, "location");
            return (Criteria) this;
        }

        public Criteria andMobileIsNull() {
            addCriterion("mobile is null");
            return (Criteria) this;
        }

        public Criteria andMobileIsNotNull() {
            addCriterion("mobile is not null");
            return (Criteria) this;
        }

        public Criteria andMobileEqualTo(String value) {
            addCriterion("mobile =", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotEqualTo(String value) {
            addCriterion("mobile <>", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThan(String value) {
            addCriterion("mobile >", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileGreaterThanOrEqualTo(String value) {
            addCriterion("mobile >=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThan(String value) {
            addCriterion("mobile <", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLessThanOrEqualTo(String value) {
            addCriterion("mobile <=", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileLike(String value) {
            addCriterion("mobile like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotLike(String value) {
            addCriterion("mobile not like", value, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileIn(List<String> values) {
            addCriterion("mobile in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotIn(List<String> values) {
            addCriterion("mobile not in", values, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileBetween(String value1, String value2) {
            addCriterion("mobile between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andMobileNotBetween(String value1, String value2) {
            addCriterion("mobile not between", value1, value2, "mobile");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Integer value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Integer value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Integer value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Integer value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Integer value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Integer> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Integer> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Integer value1, Integer value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andLongitudeIsNull() {
            addCriterion("longitude is null");
            return (Criteria) this;
        }

        public Criteria andLongitudeIsNotNull() {
            addCriterion("longitude is not null");
            return (Criteria) this;
        }

        public Criteria andLongitudeEqualTo(BigDecimal value) {
            addCriterion("longitude =", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotEqualTo(BigDecimal value) {
            addCriterion("longitude <>", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeGreaterThan(BigDecimal value) {
            addCriterion("longitude >", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("longitude >=", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeLessThan(BigDecimal value) {
            addCriterion("longitude <", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("longitude <=", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeIn(List<BigDecimal> values) {
            addCriterion("longitude in", values, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotIn(List<BigDecimal> values) {
            addCriterion("longitude not in", values, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("longitude between", value1, value2, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("longitude not between", value1, value2, "longitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeIsNull() {
            addCriterion("latitude is null");
            return (Criteria) this;
        }

        public Criteria andLatitudeIsNotNull() {
            addCriterion("latitude is not null");
            return (Criteria) this;
        }

        public Criteria andLatitudeEqualTo(BigDecimal value) {
            addCriterion("latitude =", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotEqualTo(BigDecimal value) {
            addCriterion("latitude <>", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeGreaterThan(BigDecimal value) {
            addCriterion("latitude >", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("latitude >=", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeLessThan(BigDecimal value) {
            addCriterion("latitude <", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("latitude <=", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeIn(List<BigDecimal> values) {
            addCriterion("latitude in", values, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotIn(List<BigDecimal> values) {
            addCriterion("latitude not in", values, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("latitude between", value1, value2, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("latitude not between", value1, value2, "latitude");
            return (Criteria) this;
        }

        public Criteria andDisplayIsNull() {
            addCriterion("display is null");
            return (Criteria) this;
        }

        public Criteria andDisplayIsNotNull() {
            addCriterion("display is not null");
            return (Criteria) this;
        }

        public Criteria andDisplayEqualTo(String value) {
            addCriterion("display =", value, "display");
            return (Criteria) this;
        }

        public Criteria andDisplayNotEqualTo(String value) {
            addCriterion("display <>", value, "display");
            return (Criteria) this;
        }

        public Criteria andDisplayGreaterThan(String value) {
            addCriterion("display >", value, "display");
            return (Criteria) this;
        }

        public Criteria andDisplayGreaterThanOrEqualTo(String value) {
            addCriterion("display >=", value, "display");
            return (Criteria) this;
        }

        public Criteria andDisplayLessThan(String value) {
            addCriterion("display <", value, "display");
            return (Criteria) this;
        }

        public Criteria andDisplayLessThanOrEqualTo(String value) {
            addCriterion("display <=", value, "display");
            return (Criteria) this;
        }

        public Criteria andDisplayLike(String value) {
            addCriterion("display like", value, "display");
            return (Criteria) this;
        }

        public Criteria andDisplayNotLike(String value) {
            addCriterion("display not like", value, "display");
            return (Criteria) this;
        }

        public Criteria andDisplayIn(List<String> values) {
            addCriterion("display in", values, "display");
            return (Criteria) this;
        }

        public Criteria andDisplayNotIn(List<String> values) {
            addCriterion("display not in", values, "display");
            return (Criteria) this;
        }

        public Criteria andDisplayBetween(String value1, String value2) {
            addCriterion("display between", value1, value2, "display");
            return (Criteria) this;
        }

        public Criteria andDisplayNotBetween(String value1, String value2) {
            addCriterion("display not between", value1, value2, "display");
            return (Criteria) this;
        }

        public Criteria andNoticeIsNull() {
            addCriterion("notice is null");
            return (Criteria) this;
        }

        public Criteria andNoticeIsNotNull() {
            addCriterion("notice is not null");
            return (Criteria) this;
        }

        public Criteria andNoticeEqualTo(String value) {
            addCriterion("notice =", value, "notice");
            return (Criteria) this;
        }

        public Criteria andNoticeNotEqualTo(String value) {
            addCriterion("notice <>", value, "notice");
            return (Criteria) this;
        }

        public Criteria andNoticeGreaterThan(String value) {
            addCriterion("notice >", value, "notice");
            return (Criteria) this;
        }

        public Criteria andNoticeGreaterThanOrEqualTo(String value) {
            addCriterion("notice >=", value, "notice");
            return (Criteria) this;
        }

        public Criteria andNoticeLessThan(String value) {
            addCriterion("notice <", value, "notice");
            return (Criteria) this;
        }

        public Criteria andNoticeLessThanOrEqualTo(String value) {
            addCriterion("notice <=", value, "notice");
            return (Criteria) this;
        }

        public Criteria andNoticeLike(String value) {
            addCriterion("notice like", value, "notice");
            return (Criteria) this;
        }

        public Criteria andNoticeNotLike(String value) {
            addCriterion("notice not like", value, "notice");
            return (Criteria) this;
        }

        public Criteria andNoticeIn(List<String> values) {
            addCriterion("notice in", values, "notice");
            return (Criteria) this;
        }

        public Criteria andNoticeNotIn(List<String> values) {
            addCriterion("notice not in", values, "notice");
            return (Criteria) this;
        }

        public Criteria andNoticeBetween(String value1, String value2) {
            addCriterion("notice between", value1, value2, "notice");
            return (Criteria) this;
        }

        public Criteria andNoticeNotBetween(String value1, String value2) {
            addCriterion("notice not between", value1, value2, "notice");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNull() {
            addCriterion("start_time is null");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNotNull() {
            addCriterion("start_time is not null");
            return (Criteria) this;
        }

        public Criteria andStartTimeEqualTo(Integer value) {
            addCriterion("start_time =", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotEqualTo(Integer value) {
            addCriterion("start_time <>", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThan(Integer value) {
            addCriterion("start_time >", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("start_time >=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThan(Integer value) {
            addCriterion("start_time <", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThanOrEqualTo(Integer value) {
            addCriterion("start_time <=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeIn(List<Integer> values) {
            addCriterion("start_time in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotIn(List<Integer> values) {
            addCriterion("start_time not in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeBetween(Integer value1, Integer value2) {
            addCriterion("start_time between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("start_time not between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNull() {
            addCriterion("end_time is null");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNotNull() {
            addCriterion("end_time is not null");
            return (Criteria) this;
        }

        public Criteria andEndTimeEqualTo(Integer value) {
            addCriterion("end_time =", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotEqualTo(Integer value) {
            addCriterion("end_time <>", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThan(Integer value) {
            addCriterion("end_time >", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("end_time >=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThan(Integer value) {
            addCriterion("end_time <", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThanOrEqualTo(Integer value) {
            addCriterion("end_time <=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIn(List<Integer> values) {
            addCriterion("end_time in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotIn(List<Integer> values) {
            addCriterion("end_time not in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeBetween(Integer value1, Integer value2) {
            addCriterion("end_time between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("end_time not between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeIsNull() {
            addCriterion("delivery_time is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeIsNotNull() {
            addCriterion("delivery_time is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeEqualTo(Integer value) {
            addCriterion("delivery_time =", value, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeNotEqualTo(Integer value) {
            addCriterion("delivery_time <>", value, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeGreaterThan(Integer value) {
            addCriterion("delivery_time >", value, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("delivery_time >=", value, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeLessThan(Integer value) {
            addCriterion("delivery_time <", value, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeLessThanOrEqualTo(Integer value) {
            addCriterion("delivery_time <=", value, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeIn(List<Integer> values) {
            addCriterion("delivery_time in", values, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeNotIn(List<Integer> values) {
            addCriterion("delivery_time not in", values, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeBetween(Integer value1, Integer value2) {
            addCriterion("delivery_time between", value1, value2, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("delivery_time not between", value1, value2, "deliveryTime");
            return (Criteria) this;
        }

        public Criteria andBusinessLicenseIsNull() {
            addCriterion("business_license is null");
            return (Criteria) this;
        }

        public Criteria andBusinessLicenseIsNotNull() {
            addCriterion("business_license is not null");
            return (Criteria) this;
        }

        public Criteria andBusinessLicenseEqualTo(String value) {
            addCriterion("business_license =", value, "businessLicense");
            return (Criteria) this;
        }

        public Criteria andBusinessLicenseNotEqualTo(String value) {
            addCriterion("business_license <>", value, "businessLicense");
            return (Criteria) this;
        }

        public Criteria andBusinessLicenseGreaterThan(String value) {
            addCriterion("business_license >", value, "businessLicense");
            return (Criteria) this;
        }

        public Criteria andBusinessLicenseGreaterThanOrEqualTo(String value) {
            addCriterion("business_license >=", value, "businessLicense");
            return (Criteria) this;
        }

        public Criteria andBusinessLicenseLessThan(String value) {
            addCriterion("business_license <", value, "businessLicense");
            return (Criteria) this;
        }

        public Criteria andBusinessLicenseLessThanOrEqualTo(String value) {
            addCriterion("business_license <=", value, "businessLicense");
            return (Criteria) this;
        }

        public Criteria andBusinessLicenseLike(String value) {
            addCriterion("business_license like", value, "businessLicense");
            return (Criteria) this;
        }

        public Criteria andBusinessLicenseNotLike(String value) {
            addCriterion("business_license not like", value, "businessLicense");
            return (Criteria) this;
        }

        public Criteria andBusinessLicenseIn(List<String> values) {
            addCriterion("business_license in", values, "businessLicense");
            return (Criteria) this;
        }

        public Criteria andBusinessLicenseNotIn(List<String> values) {
            addCriterion("business_license not in", values, "businessLicense");
            return (Criteria) this;
        }

        public Criteria andBusinessLicenseBetween(String value1, String value2) {
            addCriterion("business_license between", value1, value2, "businessLicense");
            return (Criteria) this;
        }

        public Criteria andBusinessLicenseNotBetween(String value1, String value2) {
            addCriterion("business_license not between", value1, value2, "businessLicense");
            return (Criteria) this;
        }

        public Criteria andOperatingPermitIsNull() {
            addCriterion("operating_permit is null");
            return (Criteria) this;
        }

        public Criteria andOperatingPermitIsNotNull() {
            addCriterion("operating_permit is not null");
            return (Criteria) this;
        }

        public Criteria andOperatingPermitEqualTo(String value) {
            addCriterion("operating_permit =", value, "operatingPermit");
            return (Criteria) this;
        }

        public Criteria andOperatingPermitNotEqualTo(String value) {
            addCriterion("operating_permit <>", value, "operatingPermit");
            return (Criteria) this;
        }

        public Criteria andOperatingPermitGreaterThan(String value) {
            addCriterion("operating_permit >", value, "operatingPermit");
            return (Criteria) this;
        }

        public Criteria andOperatingPermitGreaterThanOrEqualTo(String value) {
            addCriterion("operating_permit >=", value, "operatingPermit");
            return (Criteria) this;
        }

        public Criteria andOperatingPermitLessThan(String value) {
            addCriterion("operating_permit <", value, "operatingPermit");
            return (Criteria) this;
        }

        public Criteria andOperatingPermitLessThanOrEqualTo(String value) {
            addCriterion("operating_permit <=", value, "operatingPermit");
            return (Criteria) this;
        }

        public Criteria andOperatingPermitLike(String value) {
            addCriterion("operating_permit like", value, "operatingPermit");
            return (Criteria) this;
        }

        public Criteria andOperatingPermitNotLike(String value) {
            addCriterion("operating_permit not like", value, "operatingPermit");
            return (Criteria) this;
        }

        public Criteria andOperatingPermitIn(List<String> values) {
            addCriterion("operating_permit in", values, "operatingPermit");
            return (Criteria) this;
        }

        public Criteria andOperatingPermitNotIn(List<String> values) {
            addCriterion("operating_permit not in", values, "operatingPermit");
            return (Criteria) this;
        }

        public Criteria andOperatingPermitBetween(String value1, String value2) {
            addCriterion("operating_permit between", value1, value2, "operatingPermit");
            return (Criteria) this;
        }

        public Criteria andOperatingPermitNotBetween(String value1, String value2) {
            addCriterion("operating_permit not between", value1, value2, "operatingPermit");
            return (Criteria) this;
        }

        public Criteria andPrintCodeIsNull() {
            addCriterion("print_code is null");
            return (Criteria) this;
        }

        public Criteria andPrintCodeIsNotNull() {
            addCriterion("print_code is not null");
            return (Criteria) this;
        }

        public Criteria andPrintCodeEqualTo(String value) {
            addCriterion("print_code =", value, "printCode");
            return (Criteria) this;
        }

        public Criteria andPrintCodeNotEqualTo(String value) {
            addCriterion("print_code <>", value, "printCode");
            return (Criteria) this;
        }

        public Criteria andPrintCodeGreaterThan(String value) {
            addCriterion("print_code >", value, "printCode");
            return (Criteria) this;
        }

        public Criteria andPrintCodeGreaterThanOrEqualTo(String value) {
            addCriterion("print_code >=", value, "printCode");
            return (Criteria) this;
        }

        public Criteria andPrintCodeLessThan(String value) {
            addCriterion("print_code <", value, "printCode");
            return (Criteria) this;
        }

        public Criteria andPrintCodeLessThanOrEqualTo(String value) {
            addCriterion("print_code <=", value, "printCode");
            return (Criteria) this;
        }

        public Criteria andPrintCodeLike(String value) {
            addCriterion("print_code like", value, "printCode");
            return (Criteria) this;
        }

        public Criteria andPrintCodeNotLike(String value) {
            addCriterion("print_code not like", value, "printCode");
            return (Criteria) this;
        }

        public Criteria andPrintCodeIn(List<String> values) {
            addCriterion("print_code in", values, "printCode");
            return (Criteria) this;
        }

        public Criteria andPrintCodeNotIn(List<String> values) {
            addCriterion("print_code not in", values, "printCode");
            return (Criteria) this;
        }

        public Criteria andPrintCodeBetween(String value1, String value2) {
            addCriterion("print_code between", value1, value2, "printCode");
            return (Criteria) this;
        }

        public Criteria andPrintCodeNotBetween(String value1, String value2) {
            addCriterion("print_code not between", value1, value2, "printCode");
            return (Criteria) this;
        }

        public Criteria andCardMoneyIsNull() {
            addCriterion("card_money is null");
            return (Criteria) this;
        }

        public Criteria andCardMoneyIsNotNull() {
            addCriterion("card_money is not null");
            return (Criteria) this;
        }

        public Criteria andCardMoneyEqualTo(Double value) {
            addCriterion("card_money =", value, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andCardMoneyNotEqualTo(Double value) {
            addCriterion("card_money <>", value, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andCardMoneyGreaterThan(Double value) {
            addCriterion("card_money >", value, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andCardMoneyGreaterThanOrEqualTo(Double value) {
            addCriterion("card_money >=", value, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andCardMoneyLessThan(Double value) {
            addCriterion("card_money <", value, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andCardMoneyLessThanOrEqualTo(Double value) {
            addCriterion("card_money <=", value, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andCardMoneyIn(List<Double> values) {
            addCriterion("card_money in", values, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andCardMoneyNotIn(List<Double> values) {
            addCriterion("card_money not in", values, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andCardMoneyBetween(Double value1, Double value2) {
            addCriterion("card_money between", value1, value2, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andCardMoneyNotBetween(Double value1, Double value2) {
            addCriterion("card_money not between", value1, value2, "cardMoney");
            return (Criteria) this;
        }

        public Criteria andCardActivityIsNull() {
            addCriterion("card_activity is null");
            return (Criteria) this;
        }

        public Criteria andCardActivityIsNotNull() {
            addCriterion("card_activity is not null");
            return (Criteria) this;
        }

        public Criteria andCardActivityEqualTo(String value) {
            addCriterion("card_activity =", value, "cardActivity");
            return (Criteria) this;
        }

        public Criteria andCardActivityNotEqualTo(String value) {
            addCriterion("card_activity <>", value, "cardActivity");
            return (Criteria) this;
        }

        public Criteria andCardActivityGreaterThan(String value) {
            addCriterion("card_activity >", value, "cardActivity");
            return (Criteria) this;
        }

        public Criteria andCardActivityGreaterThanOrEqualTo(String value) {
            addCriterion("card_activity >=", value, "cardActivity");
            return (Criteria) this;
        }

        public Criteria andCardActivityLessThan(String value) {
            addCriterion("card_activity <", value, "cardActivity");
            return (Criteria) this;
        }

        public Criteria andCardActivityLessThanOrEqualTo(String value) {
            addCriterion("card_activity <=", value, "cardActivity");
            return (Criteria) this;
        }

        public Criteria andCardActivityLike(String value) {
            addCriterion("card_activity like", value, "cardActivity");
            return (Criteria) this;
        }

        public Criteria andCardActivityNotLike(String value) {
            addCriterion("card_activity not like", value, "cardActivity");
            return (Criteria) this;
        }

        public Criteria andCardActivityIn(List<String> values) {
            addCriterion("card_activity in", values, "cardActivity");
            return (Criteria) this;
        }

        public Criteria andCardActivityNotIn(List<String> values) {
            addCriterion("card_activity not in", values, "cardActivity");
            return (Criteria) this;
        }

        public Criteria andCardActivityBetween(String value1, String value2) {
            addCriterion("card_activity between", value1, value2, "cardActivity");
            return (Criteria) this;
        }

        public Criteria andCardActivityNotBetween(String value1, String value2) {
            addCriterion("card_activity not between", value1, value2, "cardActivity");
            return (Criteria) this;
        }

        public Criteria andBiddingMoneyIsNull() {
            addCriterion("bidding_money is null");
            return (Criteria) this;
        }

        public Criteria andBiddingMoneyIsNotNull() {
            addCriterion("bidding_money is not null");
            return (Criteria) this;
        }

        public Criteria andBiddingMoneyEqualTo(Double value) {
            addCriterion("bidding_money =", value, "biddingMoney");
            return (Criteria) this;
        }

        public Criteria andBiddingMoneyNotEqualTo(Double value) {
            addCriterion("bidding_money <>", value, "biddingMoney");
            return (Criteria) this;
        }

        public Criteria andBiddingMoneyGreaterThan(Double value) {
            addCriterion("bidding_money >", value, "biddingMoney");
            return (Criteria) this;
        }

        public Criteria andBiddingMoneyGreaterThanOrEqualTo(Double value) {
            addCriterion("bidding_money >=", value, "biddingMoney");
            return (Criteria) this;
        }

        public Criteria andBiddingMoneyLessThan(Double value) {
            addCriterion("bidding_money <", value, "biddingMoney");
            return (Criteria) this;
        }

        public Criteria andBiddingMoneyLessThanOrEqualTo(Double value) {
            addCriterion("bidding_money <=", value, "biddingMoney");
            return (Criteria) this;
        }

        public Criteria andBiddingMoneyIn(List<Double> values) {
            addCriterion("bidding_money in", values, "biddingMoney");
            return (Criteria) this;
        }

        public Criteria andBiddingMoneyNotIn(List<Double> values) {
            addCriterion("bidding_money not in", values, "biddingMoney");
            return (Criteria) this;
        }

        public Criteria andBiddingMoneyBetween(Double value1, Double value2) {
            addCriterion("bidding_money between", value1, value2, "biddingMoney");
            return (Criteria) this;
        }

        public Criteria andBiddingMoneyNotBetween(Double value1, Double value2) {
            addCriterion("bidding_money not between", value1, value2, "biddingMoney");
            return (Criteria) this;
        }

        public Criteria andLogoUrlIsNull() {
            addCriterion("logo_url is null");
            return (Criteria) this;
        }

        public Criteria andLogoUrlIsNotNull() {
            addCriterion("logo_url is not null");
            return (Criteria) this;
        }

        public Criteria andLogoUrlEqualTo(String value) {
            addCriterion("logo_url =", value, "logoUrl");
            return (Criteria) this;
        }

        public Criteria andLogoUrlNotEqualTo(String value) {
            addCriterion("logo_url <>", value, "logoUrl");
            return (Criteria) this;
        }

        public Criteria andLogoUrlGreaterThan(String value) {
            addCriterion("logo_url >", value, "logoUrl");
            return (Criteria) this;
        }

        public Criteria andLogoUrlGreaterThanOrEqualTo(String value) {
            addCriterion("logo_url >=", value, "logoUrl");
            return (Criteria) this;
        }

        public Criteria andLogoUrlLessThan(String value) {
            addCriterion("logo_url <", value, "logoUrl");
            return (Criteria) this;
        }

        public Criteria andLogoUrlLessThanOrEqualTo(String value) {
            addCriterion("logo_url <=", value, "logoUrl");
            return (Criteria) this;
        }

        public Criteria andLogoUrlLike(String value) {
            addCriterion("logo_url like", value, "logoUrl");
            return (Criteria) this;
        }

        public Criteria andLogoUrlNotLike(String value) {
            addCriterion("logo_url not like", value, "logoUrl");
            return (Criteria) this;
        }

        public Criteria andLogoUrlIn(List<String> values) {
            addCriterion("logo_url in", values, "logoUrl");
            return (Criteria) this;
        }

        public Criteria andLogoUrlNotIn(List<String> values) {
            addCriterion("logo_url not in", values, "logoUrl");
            return (Criteria) this;
        }

        public Criteria andLogoUrlBetween(String value1, String value2) {
            addCriterion("logo_url between", value1, value2, "logoUrl");
            return (Criteria) this;
        }

        public Criteria andLogoUrlNotBetween(String value1, String value2) {
            addCriterion("logo_url not between", value1, value2, "logoUrl");
            return (Criteria) this;
        }

        public Criteria andPromotionIsNull() {
            addCriterion("promotion is null");
            return (Criteria) this;
        }

        public Criteria andPromotionIsNotNull() {
            addCriterion("promotion is not null");
            return (Criteria) this;
        }

        public Criteria andPromotionEqualTo(String value) {
            addCriterion("promotion =", value, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionNotEqualTo(String value) {
            addCriterion("promotion <>", value, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionGreaterThan(String value) {
            addCriterion("promotion >", value, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionGreaterThanOrEqualTo(String value) {
            addCriterion("promotion >=", value, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionLessThan(String value) {
            addCriterion("promotion <", value, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionLessThanOrEqualTo(String value) {
            addCriterion("promotion <=", value, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionLike(String value) {
            addCriterion("promotion like", value, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionNotLike(String value) {
            addCriterion("promotion not like", value, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionIn(List<String> values) {
            addCriterion("promotion in", values, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionNotIn(List<String> values) {
            addCriterion("promotion not in", values, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionBetween(String value1, String value2) {
            addCriterion("promotion between", value1, value2, "promotion");
            return (Criteria) this;
        }

        public Criteria andPromotionNotBetween(String value1, String value2) {
            addCriterion("promotion not between", value1, value2, "promotion");
            return (Criteria) this;
        }

        public Criteria andCostDeliveryIsNull() {
            addCriterion("cost_delivery is null");
            return (Criteria) this;
        }

        public Criteria andCostDeliveryIsNotNull() {
            addCriterion("cost_delivery is not null");
            return (Criteria) this;
        }

        public Criteria andCostDeliveryEqualTo(Double value) {
            addCriterion("cost_delivery =", value, "costDelivery");
            return (Criteria) this;
        }

        public Criteria andCostDeliveryNotEqualTo(Double value) {
            addCriterion("cost_delivery <>", value, "costDelivery");
            return (Criteria) this;
        }

        public Criteria andCostDeliveryGreaterThan(Double value) {
            addCriterion("cost_delivery >", value, "costDelivery");
            return (Criteria) this;
        }

        public Criteria andCostDeliveryGreaterThanOrEqualTo(Double value) {
            addCriterion("cost_delivery >=", value, "costDelivery");
            return (Criteria) this;
        }

        public Criteria andCostDeliveryLessThan(Double value) {
            addCriterion("cost_delivery <", value, "costDelivery");
            return (Criteria) this;
        }

        public Criteria andCostDeliveryLessThanOrEqualTo(Double value) {
            addCriterion("cost_delivery <=", value, "costDelivery");
            return (Criteria) this;
        }

        public Criteria andCostDeliveryIn(List<Double> values) {
            addCriterion("cost_delivery in", values, "costDelivery");
            return (Criteria) this;
        }

        public Criteria andCostDeliveryNotIn(List<Double> values) {
            addCriterion("cost_delivery not in", values, "costDelivery");
            return (Criteria) this;
        }

        public Criteria andCostDeliveryBetween(Double value1, Double value2) {
            addCriterion("cost_delivery between", value1, value2, "costDelivery");
            return (Criteria) this;
        }

        public Criteria andCostDeliveryNotBetween(Double value1, Double value2) {
            addCriterion("cost_delivery not between", value1, value2, "costDelivery");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(String value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(String value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(String value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(String value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(String value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(String value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLike(String value) {
            addCriterion("type like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotLike(String value) {
            addCriterion("type not like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<String> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<String> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(String value1, String value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(String value1, String value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginIsNull() {
            addCriterion("delivery_begin is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginIsNotNull() {
            addCriterion("delivery_begin is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginEqualTo(Double value) {
            addCriterion("delivery_begin =", value, "deliveryBegin");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginNotEqualTo(Double value) {
            addCriterion("delivery_begin <>", value, "deliveryBegin");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginGreaterThan(Double value) {
            addCriterion("delivery_begin >", value, "deliveryBegin");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginGreaterThanOrEqualTo(Double value) {
            addCriterion("delivery_begin >=", value, "deliveryBegin");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginLessThan(Double value) {
            addCriterion("delivery_begin <", value, "deliveryBegin");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginLessThanOrEqualTo(Double value) {
            addCriterion("delivery_begin <=", value, "deliveryBegin");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginIn(List<Double> values) {
            addCriterion("delivery_begin in", values, "deliveryBegin");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginNotIn(List<Double> values) {
            addCriterion("delivery_begin not in", values, "deliveryBegin");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginBetween(Double value1, Double value2) {
            addCriterion("delivery_begin between", value1, value2, "deliveryBegin");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginNotBetween(Double value1, Double value2) {
            addCriterion("delivery_begin not between", value1, value2, "deliveryBegin");
            return (Criteria) this;
        }

        public Criteria andDeductionIsNull() {
            addCriterion("deduction is null");
            return (Criteria) this;
        }

        public Criteria andDeductionIsNotNull() {
            addCriterion("deduction is not null");
            return (Criteria) this;
        }

        public Criteria andDeductionEqualTo(Double value) {
            addCriterion("deduction =", value, "deduction");
            return (Criteria) this;
        }

        public Criteria andDeductionNotEqualTo(Double value) {
            addCriterion("deduction <>", value, "deduction");
            return (Criteria) this;
        }

        public Criteria andDeductionGreaterThan(Double value) {
            addCriterion("deduction >", value, "deduction");
            return (Criteria) this;
        }

        public Criteria andDeductionGreaterThanOrEqualTo(Double value) {
            addCriterion("deduction >=", value, "deduction");
            return (Criteria) this;
        }

        public Criteria andDeductionLessThan(Double value) {
            addCriterion("deduction <", value, "deduction");
            return (Criteria) this;
        }

        public Criteria andDeductionLessThanOrEqualTo(Double value) {
            addCriterion("deduction <=", value, "deduction");
            return (Criteria) this;
        }

        public Criteria andDeductionIn(List<Double> values) {
            addCriterion("deduction in", values, "deduction");
            return (Criteria) this;
        }

        public Criteria andDeductionNotIn(List<Double> values) {
            addCriterion("deduction not in", values, "deduction");
            return (Criteria) this;
        }

        public Criteria andDeductionBetween(Double value1, Double value2) {
            addCriterion("deduction between", value1, value2, "deduction");
            return (Criteria) this;
        }

        public Criteria andDeductionNotBetween(Double value1, Double value2) {
            addCriterion("deduction not between", value1, value2, "deduction");
            return (Criteria) this;
        }

        public Criteria andOrderNumIsNull() {
            addCriterion("order_num is null");
            return (Criteria) this;
        }

        public Criteria andOrderNumIsNotNull() {
            addCriterion("order_num is not null");
            return (Criteria) this;
        }

        public Criteria andOrderNumEqualTo(Integer value) {
            addCriterion("order_num =", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumNotEqualTo(Integer value) {
            addCriterion("order_num <>", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumGreaterThan(Integer value) {
            addCriterion("order_num >", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("order_num >=", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumLessThan(Integer value) {
            addCriterion("order_num <", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumLessThanOrEqualTo(Integer value) {
            addCriterion("order_num <=", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumIn(List<Integer> values) {
            addCriterion("order_num in", values, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumNotIn(List<Integer> values) {
            addCriterion("order_num not in", values, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumBetween(Integer value1, Integer value2) {
            addCriterion("order_num between", value1, value2, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumNotBetween(Integer value1, Integer value2) {
            addCriterion("order_num not between", value1, value2, "orderNum");
            return (Criteria) this;
        }

        public Criteria andIncomeDateIsNull() {
            addCriterion("income_date is null");
            return (Criteria) this;
        }

        public Criteria andIncomeDateIsNotNull() {
            addCriterion("income_date is not null");
            return (Criteria) this;
        }

        public Criteria andIncomeDateEqualTo(Integer value) {
            addCriterion("income_date =", value, "incomeDate");
            return (Criteria) this;
        }

        public Criteria andIncomeDateNotEqualTo(Integer value) {
            addCriterion("income_date <>", value, "incomeDate");
            return (Criteria) this;
        }

        public Criteria andIncomeDateGreaterThan(Integer value) {
            addCriterion("income_date >", value, "incomeDate");
            return (Criteria) this;
        }

        public Criteria andIncomeDateGreaterThanOrEqualTo(Integer value) {
            addCriterion("income_date >=", value, "incomeDate");
            return (Criteria) this;
        }

        public Criteria andIncomeDateLessThan(Integer value) {
            addCriterion("income_date <", value, "incomeDate");
            return (Criteria) this;
        }

        public Criteria andIncomeDateLessThanOrEqualTo(Integer value) {
            addCriterion("income_date <=", value, "incomeDate");
            return (Criteria) this;
        }

        public Criteria andIncomeDateIn(List<Integer> values) {
            addCriterion("income_date in", values, "incomeDate");
            return (Criteria) this;
        }

        public Criteria andIncomeDateNotIn(List<Integer> values) {
            addCriterion("income_date not in", values, "incomeDate");
            return (Criteria) this;
        }

        public Criteria andIncomeDateBetween(Integer value1, Integer value2) {
            addCriterion("income_date between", value1, value2, "incomeDate");
            return (Criteria) this;
        }

        public Criteria andIncomeDateNotBetween(Integer value1, Integer value2) {
            addCriterion("income_date not between", value1, value2, "incomeDate");
            return (Criteria) this;
        }

        public Criteria andDineOrderPrintIsNull() {
            addCriterion("dine_order_print is null");
            return (Criteria) this;
        }

        public Criteria andDineOrderPrintIsNotNull() {
            addCriterion("dine_order_print is not null");
            return (Criteria) this;
        }

        public Criteria andDineOrderPrintEqualTo(String value) {
            addCriterion("dine_order_print =", value, "dineOrderPrint");
            return (Criteria) this;
        }

        public Criteria andDineOrderPrintNotEqualTo(String value) {
            addCriterion("dine_order_print <>", value, "dineOrderPrint");
            return (Criteria) this;
        }

        public Criteria andDineOrderPrintGreaterThan(String value) {
            addCriterion("dine_order_print >", value, "dineOrderPrint");
            return (Criteria) this;
        }

        public Criteria andDineOrderPrintGreaterThanOrEqualTo(String value) {
            addCriterion("dine_order_print >=", value, "dineOrderPrint");
            return (Criteria) this;
        }

        public Criteria andDineOrderPrintLessThan(String value) {
            addCriterion("dine_order_print <", value, "dineOrderPrint");
            return (Criteria) this;
        }

        public Criteria andDineOrderPrintLessThanOrEqualTo(String value) {
            addCriterion("dine_order_print <=", value, "dineOrderPrint");
            return (Criteria) this;
        }

        public Criteria andDineOrderPrintLike(String value) {
            addCriterion("dine_order_print like", value, "dineOrderPrint");
            return (Criteria) this;
        }

        public Criteria andDineOrderPrintNotLike(String value) {
            addCriterion("dine_order_print not like", value, "dineOrderPrint");
            return (Criteria) this;
        }

        public Criteria andDineOrderPrintIn(List<String> values) {
            addCriterion("dine_order_print in", values, "dineOrderPrint");
            return (Criteria) this;
        }

        public Criteria andDineOrderPrintNotIn(List<String> values) {
            addCriterion("dine_order_print not in", values, "dineOrderPrint");
            return (Criteria) this;
        }

        public Criteria andDineOrderPrintBetween(String value1, String value2) {
            addCriterion("dine_order_print between", value1, value2, "dineOrderPrint");
            return (Criteria) this;
        }

        public Criteria andDineOrderPrintNotBetween(String value1, String value2) {
            addCriterion("dine_order_print not between", value1, value2, "dineOrderPrint");
            return (Criteria) this;
        }

        public Criteria andNoticeTimeIsNull() {
            addCriterion("notice_time is null");
            return (Criteria) this;
        }

        public Criteria andNoticeTimeIsNotNull() {
            addCriterion("notice_time is not null");
            return (Criteria) this;
        }

        public Criteria andNoticeTimeEqualTo(Date value) {
            addCriterion("notice_time =", value, "noticeTime");
            return (Criteria) this;
        }

        public Criteria andNoticeTimeNotEqualTo(Date value) {
            addCriterion("notice_time <>", value, "noticeTime");
            return (Criteria) this;
        }

        public Criteria andNoticeTimeGreaterThan(Date value) {
            addCriterion("notice_time >", value, "noticeTime");
            return (Criteria) this;
        }

        public Criteria andNoticeTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("notice_time >=", value, "noticeTime");
            return (Criteria) this;
        }

        public Criteria andNoticeTimeLessThan(Date value) {
            addCriterion("notice_time <", value, "noticeTime");
            return (Criteria) this;
        }

        public Criteria andNoticeTimeLessThanOrEqualTo(Date value) {
            addCriterion("notice_time <=", value, "noticeTime");
            return (Criteria) this;
        }

        public Criteria andNoticeTimeIn(List<Date> values) {
            addCriterion("notice_time in", values, "noticeTime");
            return (Criteria) this;
        }

        public Criteria andNoticeTimeNotIn(List<Date> values) {
            addCriterion("notice_time not in", values, "noticeTime");
            return (Criteria) this;
        }

        public Criteria andNoticeTimeBetween(Date value1, Date value2) {
            addCriterion("notice_time between", value1, value2, "noticeTime");
            return (Criteria) this;
        }

        public Criteria andNoticeTimeNotBetween(Date value1, Date value2) {
            addCriterion("notice_time not between", value1, value2, "noticeTime");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNull() {
            addCriterion("is_delete is null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNotNull() {
            addCriterion("is_delete is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteEqualTo(Byte value) {
            addCriterion("is_delete =", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotEqualTo(Byte value) {
            addCriterion("is_delete <>", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThan(Byte value) {
            addCriterion("is_delete >", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_delete >=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThan(Byte value) {
            addCriterion("is_delete <", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThanOrEqualTo(Byte value) {
            addCriterion("is_delete <=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIn(List<Byte> values) {
            addCriterion("is_delete in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotIn(List<Byte> values) {
            addCriterion("is_delete not in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteBetween(Byte value1, Byte value2) {
            addCriterion("is_delete between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotBetween(Byte value1, Byte value2) {
            addCriterion("is_delete not between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andMobileUpdateTimeIsNull() {
            addCriterion("mobile_update_time is null");
            return (Criteria) this;
        }

        public Criteria andMobileUpdateTimeIsNotNull() {
            addCriterion("mobile_update_time is not null");
            return (Criteria) this;
        }

        public Criteria andMobileUpdateTimeEqualTo(Date value) {
            addCriterion("mobile_update_time =", value, "mobileUpdateTime");
            return (Criteria) this;
        }

        public Criteria andMobileUpdateTimeNotEqualTo(Date value) {
            addCriterion("mobile_update_time <>", value, "mobileUpdateTime");
            return (Criteria) this;
        }

        public Criteria andMobileUpdateTimeGreaterThan(Date value) {
            addCriterion("mobile_update_time >", value, "mobileUpdateTime");
            return (Criteria) this;
        }

        public Criteria andMobileUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("mobile_update_time >=", value, "mobileUpdateTime");
            return (Criteria) this;
        }

        public Criteria andMobileUpdateTimeLessThan(Date value) {
            addCriterion("mobile_update_time <", value, "mobileUpdateTime");
            return (Criteria) this;
        }

        public Criteria andMobileUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("mobile_update_time <=", value, "mobileUpdateTime");
            return (Criteria) this;
        }

        public Criteria andMobileUpdateTimeIn(List<Date> values) {
            addCriterion("mobile_update_time in", values, "mobileUpdateTime");
            return (Criteria) this;
        }

        public Criteria andMobileUpdateTimeNotIn(List<Date> values) {
            addCriterion("mobile_update_time not in", values, "mobileUpdateTime");
            return (Criteria) this;
        }

        public Criteria andMobileUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("mobile_update_time between", value1, value2, "mobileUpdateTime");
            return (Criteria) this;
        }

        public Criteria andMobileUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("mobile_update_time not between", value1, value2, "mobileUpdateTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginUpdateTimeIsNull() {
            addCriterion("delivery_begin_update_time is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginUpdateTimeIsNotNull() {
            addCriterion("delivery_begin_update_time is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginUpdateTimeEqualTo(Date value) {
            addCriterion("delivery_begin_update_time =", value, "deliveryBeginUpdateTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginUpdateTimeNotEqualTo(Date value) {
            addCriterion("delivery_begin_update_time <>", value, "deliveryBeginUpdateTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginUpdateTimeGreaterThan(Date value) {
            addCriterion("delivery_begin_update_time >", value, "deliveryBeginUpdateTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("delivery_begin_update_time >=", value, "deliveryBeginUpdateTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginUpdateTimeLessThan(Date value) {
            addCriterion("delivery_begin_update_time <", value, "deliveryBeginUpdateTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("delivery_begin_update_time <=", value, "deliveryBeginUpdateTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginUpdateTimeIn(List<Date> values) {
            addCriterion("delivery_begin_update_time in", values, "deliveryBeginUpdateTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginUpdateTimeNotIn(List<Date> values) {
            addCriterion("delivery_begin_update_time not in", values, "deliveryBeginUpdateTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("delivery_begin_update_time between", value1, value2, "deliveryBeginUpdateTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryBeginUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("delivery_begin_update_time not between", value1, value2, "deliveryBeginUpdateTime");
            return (Criteria) this;
        }

        public Criteria andAlipayLimitIsNull() {
            addCriterion("alipay_limit is null");
            return (Criteria) this;
        }

        public Criteria andAlipayLimitIsNotNull() {
            addCriterion("alipay_limit is not null");
            return (Criteria) this;
        }

        public Criteria andAlipayLimitEqualTo(Integer value) {
            addCriterion("alipay_limit =", value, "alipayLimit");
            return (Criteria) this;
        }

        public Criteria andAlipayLimitNotEqualTo(Integer value) {
            addCriterion("alipay_limit <>", value, "alipayLimit");
            return (Criteria) this;
        }

        public Criteria andAlipayLimitGreaterThan(Integer value) {
            addCriterion("alipay_limit >", value, "alipayLimit");
            return (Criteria) this;
        }

        public Criteria andAlipayLimitGreaterThanOrEqualTo(Integer value) {
            addCriterion("alipay_limit >=", value, "alipayLimit");
            return (Criteria) this;
        }

        public Criteria andAlipayLimitLessThan(Integer value) {
            addCriterion("alipay_limit <", value, "alipayLimit");
            return (Criteria) this;
        }

        public Criteria andAlipayLimitLessThanOrEqualTo(Integer value) {
            addCriterion("alipay_limit <=", value, "alipayLimit");
            return (Criteria) this;
        }

        public Criteria andAlipayLimitIn(List<Integer> values) {
            addCriterion("alipay_limit in", values, "alipayLimit");
            return (Criteria) this;
        }

        public Criteria andAlipayLimitNotIn(List<Integer> values) {
            addCriterion("alipay_limit not in", values, "alipayLimit");
            return (Criteria) this;
        }

        public Criteria andAlipayLimitBetween(Integer value1, Integer value2) {
            addCriterion("alipay_limit between", value1, value2, "alipayLimit");
            return (Criteria) this;
        }

        public Criteria andAlipayLimitNotBetween(Integer value1, Integer value2) {
            addCriterion("alipay_limit not between", value1, value2, "alipayLimit");
            return (Criteria) this;
        }

        public Criteria andWechatLimitIsNull() {
            addCriterion("wechat_limit is null");
            return (Criteria) this;
        }

        public Criteria andWechatLimitIsNotNull() {
            addCriterion("wechat_limit is not null");
            return (Criteria) this;
        }

        public Criteria andWechatLimitEqualTo(Integer value) {
            addCriterion("wechat_limit =", value, "wechatLimit");
            return (Criteria) this;
        }

        public Criteria andWechatLimitNotEqualTo(Integer value) {
            addCriterion("wechat_limit <>", value, "wechatLimit");
            return (Criteria) this;
        }

        public Criteria andWechatLimitGreaterThan(Integer value) {
            addCriterion("wechat_limit >", value, "wechatLimit");
            return (Criteria) this;
        }

        public Criteria andWechatLimitGreaterThanOrEqualTo(Integer value) {
            addCriterion("wechat_limit >=", value, "wechatLimit");
            return (Criteria) this;
        }

        public Criteria andWechatLimitLessThan(Integer value) {
            addCriterion("wechat_limit <", value, "wechatLimit");
            return (Criteria) this;
        }

        public Criteria andWechatLimitLessThanOrEqualTo(Integer value) {
            addCriterion("wechat_limit <=", value, "wechatLimit");
            return (Criteria) this;
        }

        public Criteria andWechatLimitIn(List<Integer> values) {
            addCriterion("wechat_limit in", values, "wechatLimit");
            return (Criteria) this;
        }

        public Criteria andWechatLimitNotIn(List<Integer> values) {
            addCriterion("wechat_limit not in", values, "wechatLimit");
            return (Criteria) this;
        }

        public Criteria andWechatLimitBetween(Integer value1, Integer value2) {
            addCriterion("wechat_limit between", value1, value2, "wechatLimit");
            return (Criteria) this;
        }

        public Criteria andWechatLimitNotBetween(Integer value1, Integer value2) {
            addCriterion("wechat_limit not between", value1, value2, "wechatLimit");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}