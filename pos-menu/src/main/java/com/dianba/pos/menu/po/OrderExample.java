package com.dianba.pos.menu.po;

import java.util.ArrayList;
import java.util.List;

public class OrderExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OrderExample() {
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

        public Criteria andPayIdIsNull() {
            addCriterion("pay_id is null");
            return (Criteria) this;
        }

        public Criteria andPayIdIsNotNull() {
            addCriterion("pay_id is not null");
            return (Criteria) this;
        }

        public Criteria andPayIdEqualTo(String value) {
            addCriterion("pay_id =", value, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdNotEqualTo(String value) {
            addCriterion("pay_id <>", value, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdGreaterThan(String value) {
            addCriterion("pay_id >", value, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdGreaterThanOrEqualTo(String value) {
            addCriterion("pay_id >=", value, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdLessThan(String value) {
            addCriterion("pay_id <", value, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdLessThanOrEqualTo(String value) {
            addCriterion("pay_id <=", value, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdLike(String value) {
            addCriterion("pay_id like", value, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdNotLike(String value) {
            addCriterion("pay_id not like", value, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdIn(List<String> values) {
            addCriterion("pay_id in", values, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdNotIn(List<String> values) {
            addCriterion("pay_id not in", values, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdBetween(String value1, String value2) {
            addCriterion("pay_id between", value1, value2, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdNotBetween(String value1, String value2) {
            addCriterion("pay_id not between", value1, value2, "payId");
            return (Criteria) this;
        }

        public Criteria andPayTypeIsNull() {
            addCriterion("pay_type is null");
            return (Criteria) this;
        }

        public Criteria andPayTypeIsNotNull() {
            addCriterion("pay_type is not null");
            return (Criteria) this;
        }

        public Criteria andPayTypeEqualTo(String value) {
            addCriterion("pay_type =", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotEqualTo(String value) {
            addCriterion("pay_type <>", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeGreaterThan(String value) {
            addCriterion("pay_type >", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeGreaterThanOrEqualTo(String value) {
            addCriterion("pay_type >=", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLessThan(String value) {
            addCriterion("pay_type <", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLessThanOrEqualTo(String value) {
            addCriterion("pay_type <=", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLike(String value) {
            addCriterion("pay_type like", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotLike(String value) {
            addCriterion("pay_type not like", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeIn(List<String> values) {
            addCriterion("pay_type in", values, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotIn(List<String> values) {
            addCriterion("pay_type not in", values, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeBetween(String value1, String value2) {
            addCriterion("pay_type between", value1, value2, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotBetween(String value1, String value2) {
            addCriterion("pay_type not between", value1, value2, "payType");
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

        public Criteria andCourierIdIsNull() {
            addCriterion("courier_id is null");
            return (Criteria) this;
        }

        public Criteria andCourierIdIsNotNull() {
            addCriterion("courier_id is not null");
            return (Criteria) this;
        }

        public Criteria andCourierIdEqualTo(Long value) {
            addCriterion("courier_id =", value, "courierId");
            return (Criteria) this;
        }

        public Criteria andCourierIdNotEqualTo(Long value) {
            addCriterion("courier_id <>", value, "courierId");
            return (Criteria) this;
        }

        public Criteria andCourierIdGreaterThan(Long value) {
            addCriterion("courier_id >", value, "courierId");
            return (Criteria) this;
        }

        public Criteria andCourierIdGreaterThanOrEqualTo(Long value) {
            addCriterion("courier_id >=", value, "courierId");
            return (Criteria) this;
        }

        public Criteria andCourierIdLessThan(Long value) {
            addCriterion("courier_id <", value, "courierId");
            return (Criteria) this;
        }

        public Criteria andCourierIdLessThanOrEqualTo(Long value) {
            addCriterion("courier_id <=", value, "courierId");
            return (Criteria) this;
        }

        public Criteria andCourierIdIn(List<Long> values) {
            addCriterion("courier_id in", values, "courierId");
            return (Criteria) this;
        }

        public Criteria andCourierIdNotIn(List<Long> values) {
            addCriterion("courier_id not in", values, "courierId");
            return (Criteria) this;
        }

        public Criteria andCourierIdBetween(Long value1, Long value2) {
            addCriterion("courier_id between", value1, value2, "courierId");
            return (Criteria) this;
        }

        public Criteria andCourierIdNotBetween(Long value1, Long value2) {
            addCriterion("courier_id not between", value1, value2, "courierId");
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

        public Criteria andCardIdIsNull() {
            addCriterion("card_id is null");
            return (Criteria) this;
        }

        public Criteria andCardIdIsNotNull() {
            addCriterion("card_id is not null");
            return (Criteria) this;
        }

        public Criteria andCardIdEqualTo(String value) {
            addCriterion("card_id =", value, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdNotEqualTo(String value) {
            addCriterion("card_id <>", value, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdGreaterThan(String value) {
            addCriterion("card_id >", value, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdGreaterThanOrEqualTo(String value) {
            addCriterion("card_id >=", value, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdLessThan(String value) {
            addCriterion("card_id <", value, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdLessThanOrEqualTo(String value) {
            addCriterion("card_id <=", value, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdLike(String value) {
            addCriterion("card_id like", value, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdNotLike(String value) {
            addCriterion("card_id not like", value, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdIn(List<String> values) {
            addCriterion("card_id in", values, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdNotIn(List<String> values) {
            addCriterion("card_id not in", values, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdBetween(String value1, String value2) {
            addCriterion("card_id between", value1, value2, "cardId");
            return (Criteria) this;
        }

        public Criteria andCardIdNotBetween(String value1, String value2) {
            addCriterion("card_id not between", value1, value2, "cardId");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("status like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("status not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStateIsNull() {
            addCriterion("state is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("state is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(String value) {
            addCriterion("state =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(String value) {
            addCriterion("state <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(String value) {
            addCriterion("state >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(String value) {
            addCriterion("state >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(String value) {
            addCriterion("state <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(String value) {
            addCriterion("state <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLike(String value) {
            addCriterion("state like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotLike(String value) {
            addCriterion("state not like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<String> values) {
            addCriterion("state in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<String> values) {
            addCriterion("state not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(String value1, String value2) {
            addCriterion("state between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(String value1, String value2) {
            addCriterion("state not between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andRstateIsNull() {
            addCriterion("rstate is null");
            return (Criteria) this;
        }

        public Criteria andRstateIsNotNull() {
            addCriterion("rstate is not null");
            return (Criteria) this;
        }

        public Criteria andRstateEqualTo(String value) {
            addCriterion("rstate =", value, "rstate");
            return (Criteria) this;
        }

        public Criteria andRstateNotEqualTo(String value) {
            addCriterion("rstate <>", value, "rstate");
            return (Criteria) this;
        }

        public Criteria andRstateGreaterThan(String value) {
            addCriterion("rstate >", value, "rstate");
            return (Criteria) this;
        }

        public Criteria andRstateGreaterThanOrEqualTo(String value) {
            addCriterion("rstate >=", value, "rstate");
            return (Criteria) this;
        }

        public Criteria andRstateLessThan(String value) {
            addCriterion("rstate <", value, "rstate");
            return (Criteria) this;
        }

        public Criteria andRstateLessThanOrEqualTo(String value) {
            addCriterion("rstate <=", value, "rstate");
            return (Criteria) this;
        }

        public Criteria andRstateLike(String value) {
            addCriterion("rstate like", value, "rstate");
            return (Criteria) this;
        }

        public Criteria andRstateNotLike(String value) {
            addCriterion("rstate not like", value, "rstate");
            return (Criteria) this;
        }

        public Criteria andRstateIn(List<String> values) {
            addCriterion("rstate in", values, "rstate");
            return (Criteria) this;
        }

        public Criteria andRstateNotIn(List<String> values) {
            addCriterion("rstate not in", values, "rstate");
            return (Criteria) this;
        }

        public Criteria andRstateBetween(String value1, String value2) {
            addCriterion("rstate between", value1, value2, "rstate");
            return (Criteria) this;
        }

        public Criteria andRstateNotBetween(String value1, String value2) {
            addCriterion("rstate not between", value1, value2, "rstate");
            return (Criteria) this;
        }

        public Criteria andRetimeIsNull() {
            addCriterion("retime is null");
            return (Criteria) this;
        }

        public Criteria andRetimeIsNotNull() {
            addCriterion("retime is not null");
            return (Criteria) this;
        }

        public Criteria andRetimeEqualTo(Integer value) {
            addCriterion("retime =", value, "retime");
            return (Criteria) this;
        }

        public Criteria andRetimeNotEqualTo(Integer value) {
            addCriterion("retime <>", value, "retime");
            return (Criteria) this;
        }

        public Criteria andRetimeGreaterThan(Integer value) {
            addCriterion("retime >", value, "retime");
            return (Criteria) this;
        }

        public Criteria andRetimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("retime >=", value, "retime");
            return (Criteria) this;
        }

        public Criteria andRetimeLessThan(Integer value) {
            addCriterion("retime <", value, "retime");
            return (Criteria) this;
        }

        public Criteria andRetimeLessThanOrEqualTo(Integer value) {
            addCriterion("retime <=", value, "retime");
            return (Criteria) this;
        }

        public Criteria andRetimeIn(List<Integer> values) {
            addCriterion("retime in", values, "retime");
            return (Criteria) this;
        }

        public Criteria andRetimeNotIn(List<Integer> values) {
            addCriterion("retime not in", values, "retime");
            return (Criteria) this;
        }

        public Criteria andRetimeBetween(Integer value1, Integer value2) {
            addCriterion("retime between", value1, value2, "retime");
            return (Criteria) this;
        }

        public Criteria andRetimeNotBetween(Integer value1, Integer value2) {
            addCriterion("retime not between", value1, value2, "retime");
            return (Criteria) this;
        }

        public Criteria andRealnameIsNull() {
            addCriterion("realname is null");
            return (Criteria) this;
        }

        public Criteria andRealnameIsNotNull() {
            addCriterion("realname is not null");
            return (Criteria) this;
        }

        public Criteria andRealnameEqualTo(String value) {
            addCriterion("realname =", value, "realname");
            return (Criteria) this;
        }

        public Criteria andRealnameNotEqualTo(String value) {
            addCriterion("realname <>", value, "realname");
            return (Criteria) this;
        }

        public Criteria andRealnameGreaterThan(String value) {
            addCriterion("realname >", value, "realname");
            return (Criteria) this;
        }

        public Criteria andRealnameGreaterThanOrEqualTo(String value) {
            addCriterion("realname >=", value, "realname");
            return (Criteria) this;
        }

        public Criteria andRealnameLessThan(String value) {
            addCriterion("realname <", value, "realname");
            return (Criteria) this;
        }

        public Criteria andRealnameLessThanOrEqualTo(String value) {
            addCriterion("realname <=", value, "realname");
            return (Criteria) this;
        }

        public Criteria andRealnameLike(String value) {
            addCriterion("realname like", value, "realname");
            return (Criteria) this;
        }

        public Criteria andRealnameNotLike(String value) {
            addCriterion("realname not like", value, "realname");
            return (Criteria) this;
        }

        public Criteria andRealnameIn(List<String> values) {
            addCriterion("realname in", values, "realname");
            return (Criteria) this;
        }

        public Criteria andRealnameNotIn(List<String> values) {
            addCriterion("realname not in", values, "realname");
            return (Criteria) this;
        }

        public Criteria andRealnameBetween(String value1, String value2) {
            addCriterion("realname between", value1, value2, "realname");
            return (Criteria) this;
        }

        public Criteria andRealnameNotBetween(String value1, String value2) {
            addCriterion("realname not between", value1, value2, "realname");
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

        public Criteria andOnlineMoneyIsNull() {
            addCriterion("online_money is null");
            return (Criteria) this;
        }

        public Criteria andOnlineMoneyIsNotNull() {
            addCriterion("online_money is not null");
            return (Criteria) this;
        }

        public Criteria andOnlineMoneyEqualTo(Double value) {
            addCriterion("online_money =", value, "onlineMoney");
            return (Criteria) this;
        }

        public Criteria andOnlineMoneyNotEqualTo(Double value) {
            addCriterion("online_money <>", value, "onlineMoney");
            return (Criteria) this;
        }

        public Criteria andOnlineMoneyGreaterThan(Double value) {
            addCriterion("online_money >", value, "onlineMoney");
            return (Criteria) this;
        }

        public Criteria andOnlineMoneyGreaterThanOrEqualTo(Double value) {
            addCriterion("online_money >=", value, "onlineMoney");
            return (Criteria) this;
        }

        public Criteria andOnlineMoneyLessThan(Double value) {
            addCriterion("online_money <", value, "onlineMoney");
            return (Criteria) this;
        }

        public Criteria andOnlineMoneyLessThanOrEqualTo(Double value) {
            addCriterion("online_money <=", value, "onlineMoney");
            return (Criteria) this;
        }

        public Criteria andOnlineMoneyIn(List<Double> values) {
            addCriterion("online_money in", values, "onlineMoney");
            return (Criteria) this;
        }

        public Criteria andOnlineMoneyNotIn(List<Double> values) {
            addCriterion("online_money not in", values, "onlineMoney");
            return (Criteria) this;
        }

        public Criteria andOnlineMoneyBetween(Double value1, Double value2) {
            addCriterion("online_money between", value1, value2, "onlineMoney");
            return (Criteria) this;
        }

        public Criteria andOnlineMoneyNotBetween(Double value1, Double value2) {
            addCriterion("online_money not between", value1, value2, "onlineMoney");
            return (Criteria) this;
        }

        public Criteria andOriginIsNull() {
            addCriterion("origin is null");
            return (Criteria) this;
        }

        public Criteria andOriginIsNotNull() {
            addCriterion("origin is not null");
            return (Criteria) this;
        }

        public Criteria andOriginEqualTo(Double value) {
            addCriterion("origin =", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginNotEqualTo(Double value) {
            addCriterion("origin <>", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginGreaterThan(Double value) {
            addCriterion("origin >", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginGreaterThanOrEqualTo(Double value) {
            addCriterion("origin >=", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginLessThan(Double value) {
            addCriterion("origin <", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginLessThanOrEqualTo(Double value) {
            addCriterion("origin <=", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginIn(List<Double> values) {
            addCriterion("origin in", values, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginNotIn(List<Double> values) {
            addCriterion("origin not in", values, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginBetween(Double value1, Double value2) {
            addCriterion("origin between", value1, value2, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginNotBetween(Double value1, Double value2) {
            addCriterion("origin not between", value1, value2, "origin");
            return (Criteria) this;
        }

        public Criteria andCreditIsNull() {
            addCriterion("credit is null");
            return (Criteria) this;
        }

        public Criteria andCreditIsNotNull() {
            addCriterion("credit is not null");
            return (Criteria) this;
        }

        public Criteria andCreditEqualTo(Double value) {
            addCriterion("credit =", value, "credit");
            return (Criteria) this;
        }

        public Criteria andCreditNotEqualTo(Double value) {
            addCriterion("credit <>", value, "credit");
            return (Criteria) this;
        }

        public Criteria andCreditGreaterThan(Double value) {
            addCriterion("credit >", value, "credit");
            return (Criteria) this;
        }

        public Criteria andCreditGreaterThanOrEqualTo(Double value) {
            addCriterion("credit >=", value, "credit");
            return (Criteria) this;
        }

        public Criteria andCreditLessThan(Double value) {
            addCriterion("credit <", value, "credit");
            return (Criteria) this;
        }

        public Criteria andCreditLessThanOrEqualTo(Double value) {
            addCriterion("credit <=", value, "credit");
            return (Criteria) this;
        }

        public Criteria andCreditIn(List<Double> values) {
            addCriterion("credit in", values, "credit");
            return (Criteria) this;
        }

        public Criteria andCreditNotIn(List<Double> values) {
            addCriterion("credit not in", values, "credit");
            return (Criteria) this;
        }

        public Criteria andCreditBetween(Double value1, Double value2) {
            addCriterion("credit between", value1, value2, "credit");
            return (Criteria) this;
        }

        public Criteria andCreditNotBetween(Double value1, Double value2) {
            addCriterion("credit not between", value1, value2, "credit");
            return (Criteria) this;
        }

        public Criteria andCardIsNull() {
            addCriterion("card is null");
            return (Criteria) this;
        }

        public Criteria andCardIsNotNull() {
            addCriterion("card is not null");
            return (Criteria) this;
        }

        public Criteria andCardEqualTo(Double value) {
            addCriterion("card =", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardNotEqualTo(Double value) {
            addCriterion("card <>", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardGreaterThan(Double value) {
            addCriterion("card >", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardGreaterThanOrEqualTo(Double value) {
            addCriterion("card >=", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardLessThan(Double value) {
            addCriterion("card <", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardLessThanOrEqualTo(Double value) {
            addCriterion("card <=", value, "card");
            return (Criteria) this;
        }

        public Criteria andCardIn(List<Double> values) {
            addCriterion("card in", values, "card");
            return (Criteria) this;
        }

        public Criteria andCardNotIn(List<Double> values) {
            addCriterion("card not in", values, "card");
            return (Criteria) this;
        }

        public Criteria andCardBetween(Double value1, Double value2) {
            addCriterion("card between", value1, value2, "card");
            return (Criteria) this;
        }

        public Criteria andCardNotBetween(Double value1, Double value2) {
            addCriterion("card not between", value1, value2, "card");
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

        public Criteria andPayTimeIsNull() {
            addCriterion("pay_time is null");
            return (Criteria) this;
        }

        public Criteria andPayTimeIsNotNull() {
            addCriterion("pay_time is not null");
            return (Criteria) this;
        }

        public Criteria andPayTimeEqualTo(Integer value) {
            addCriterion("pay_time =", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeNotEqualTo(Integer value) {
            addCriterion("pay_time <>", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeGreaterThan(Integer value) {
            addCriterion("pay_time >", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("pay_time >=", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeLessThan(Integer value) {
            addCriterion("pay_time <", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeLessThanOrEqualTo(Integer value) {
            addCriterion("pay_time <=", value, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeIn(List<Integer> values) {
            addCriterion("pay_time in", values, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeNotIn(List<Integer> values) {
            addCriterion("pay_time not in", values, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeBetween(Integer value1, Integer value2) {
            addCriterion("pay_time between", value1, value2, "payTime");
            return (Criteria) this;
        }

        public Criteria andPayTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("pay_time not between", value1, value2, "payTime");
            return (Criteria) this;
        }

        public Criteria andCommentDisplayIsNull() {
            addCriterion("comment_display is null");
            return (Criteria) this;
        }

        public Criteria andCommentDisplayIsNotNull() {
            addCriterion("comment_display is not null");
            return (Criteria) this;
        }

        public Criteria andCommentDisplayEqualTo(String value) {
            addCriterion("comment_display =", value, "commentDisplay");
            return (Criteria) this;
        }

        public Criteria andCommentDisplayNotEqualTo(String value) {
            addCriterion("comment_display <>", value, "commentDisplay");
            return (Criteria) this;
        }

        public Criteria andCommentDisplayGreaterThan(String value) {
            addCriterion("comment_display >", value, "commentDisplay");
            return (Criteria) this;
        }

        public Criteria andCommentDisplayGreaterThanOrEqualTo(String value) {
            addCriterion("comment_display >=", value, "commentDisplay");
            return (Criteria) this;
        }

        public Criteria andCommentDisplayLessThan(String value) {
            addCriterion("comment_display <", value, "commentDisplay");
            return (Criteria) this;
        }

        public Criteria andCommentDisplayLessThanOrEqualTo(String value) {
            addCriterion("comment_display <=", value, "commentDisplay");
            return (Criteria) this;
        }

        public Criteria andCommentDisplayLike(String value) {
            addCriterion("comment_display like", value, "commentDisplay");
            return (Criteria) this;
        }

        public Criteria andCommentDisplayNotLike(String value) {
            addCriterion("comment_display not like", value, "commentDisplay");
            return (Criteria) this;
        }

        public Criteria andCommentDisplayIn(List<String> values) {
            addCriterion("comment_display in", values, "commentDisplay");
            return (Criteria) this;
        }

        public Criteria andCommentDisplayNotIn(List<String> values) {
            addCriterion("comment_display not in", values, "commentDisplay");
            return (Criteria) this;
        }

        public Criteria andCommentDisplayBetween(String value1, String value2) {
            addCriterion("comment_display between", value1, value2, "commentDisplay");
            return (Criteria) this;
        }

        public Criteria andCommentDisplayNotBetween(String value1, String value2) {
            addCriterion("comment_display not between", value1, value2, "commentDisplay");
            return (Criteria) this;
        }

        public Criteria andCommentTasteIsNull() {
            addCriterion("comment_taste is null");
            return (Criteria) this;
        }

        public Criteria andCommentTasteIsNotNull() {
            addCriterion("comment_taste is not null");
            return (Criteria) this;
        }

        public Criteria andCommentTasteEqualTo(Float value) {
            addCriterion("comment_taste =", value, "commentTaste");
            return (Criteria) this;
        }

        public Criteria andCommentTasteNotEqualTo(Float value) {
            addCriterion("comment_taste <>", value, "commentTaste");
            return (Criteria) this;
        }

        public Criteria andCommentTasteGreaterThan(Float value) {
            addCriterion("comment_taste >", value, "commentTaste");
            return (Criteria) this;
        }

        public Criteria andCommentTasteGreaterThanOrEqualTo(Float value) {
            addCriterion("comment_taste >=", value, "commentTaste");
            return (Criteria) this;
        }

        public Criteria andCommentTasteLessThan(Float value) {
            addCriterion("comment_taste <", value, "commentTaste");
            return (Criteria) this;
        }

        public Criteria andCommentTasteLessThanOrEqualTo(Float value) {
            addCriterion("comment_taste <=", value, "commentTaste");
            return (Criteria) this;
        }

        public Criteria andCommentTasteIn(List<Float> values) {
            addCriterion("comment_taste in", values, "commentTaste");
            return (Criteria) this;
        }

        public Criteria andCommentTasteNotIn(List<Float> values) {
            addCriterion("comment_taste not in", values, "commentTaste");
            return (Criteria) this;
        }

        public Criteria andCommentTasteBetween(Float value1, Float value2) {
            addCriterion("comment_taste between", value1, value2, "commentTaste");
            return (Criteria) this;
        }

        public Criteria andCommentTasteNotBetween(Float value1, Float value2) {
            addCriterion("comment_taste not between", value1, value2, "commentTaste");
            return (Criteria) this;
        }

        public Criteria andCommentSpeedIsNull() {
            addCriterion("comment_speed is null");
            return (Criteria) this;
        }

        public Criteria andCommentSpeedIsNotNull() {
            addCriterion("comment_speed is not null");
            return (Criteria) this;
        }

        public Criteria andCommentSpeedEqualTo(Float value) {
            addCriterion("comment_speed =", value, "commentSpeed");
            return (Criteria) this;
        }

        public Criteria andCommentSpeedNotEqualTo(Float value) {
            addCriterion("comment_speed <>", value, "commentSpeed");
            return (Criteria) this;
        }

        public Criteria andCommentSpeedGreaterThan(Float value) {
            addCriterion("comment_speed >", value, "commentSpeed");
            return (Criteria) this;
        }

        public Criteria andCommentSpeedGreaterThanOrEqualTo(Float value) {
            addCriterion("comment_speed >=", value, "commentSpeed");
            return (Criteria) this;
        }

        public Criteria andCommentSpeedLessThan(Float value) {
            addCriterion("comment_speed <", value, "commentSpeed");
            return (Criteria) this;
        }

        public Criteria andCommentSpeedLessThanOrEqualTo(Float value) {
            addCriterion("comment_speed <=", value, "commentSpeed");
            return (Criteria) this;
        }

        public Criteria andCommentSpeedIn(List<Float> values) {
            addCriterion("comment_speed in", values, "commentSpeed");
            return (Criteria) this;
        }

        public Criteria andCommentSpeedNotIn(List<Float> values) {
            addCriterion("comment_speed not in", values, "commentSpeed");
            return (Criteria) this;
        }

        public Criteria andCommentSpeedBetween(Float value1, Float value2) {
            addCriterion("comment_speed between", value1, value2, "commentSpeed");
            return (Criteria) this;
        }

        public Criteria andCommentSpeedNotBetween(Float value1, Float value2) {
            addCriterion("comment_speed not between", value1, value2, "commentSpeed");
            return (Criteria) this;
        }

        public Criteria andCommentServiceIsNull() {
            addCriterion("comment_service is null");
            return (Criteria) this;
        }

        public Criteria andCommentServiceIsNotNull() {
            addCriterion("comment_service is not null");
            return (Criteria) this;
        }

        public Criteria andCommentServiceEqualTo(Float value) {
            addCriterion("comment_service =", value, "commentService");
            return (Criteria) this;
        }

        public Criteria andCommentServiceNotEqualTo(Float value) {
            addCriterion("comment_service <>", value, "commentService");
            return (Criteria) this;
        }

        public Criteria andCommentServiceGreaterThan(Float value) {
            addCriterion("comment_service >", value, "commentService");
            return (Criteria) this;
        }

        public Criteria andCommentServiceGreaterThanOrEqualTo(Float value) {
            addCriterion("comment_service >=", value, "commentService");
            return (Criteria) this;
        }

        public Criteria andCommentServiceLessThan(Float value) {
            addCriterion("comment_service <", value, "commentService");
            return (Criteria) this;
        }

        public Criteria andCommentServiceLessThanOrEqualTo(Float value) {
            addCriterion("comment_service <=", value, "commentService");
            return (Criteria) this;
        }

        public Criteria andCommentServiceIn(List<Float> values) {
            addCriterion("comment_service in", values, "commentService");
            return (Criteria) this;
        }

        public Criteria andCommentServiceNotIn(List<Float> values) {
            addCriterion("comment_service not in", values, "commentService");
            return (Criteria) this;
        }

        public Criteria andCommentServiceBetween(Float value1, Float value2) {
            addCriterion("comment_service between", value1, value2, "commentService");
            return (Criteria) this;
        }

        public Criteria andCommentServiceNotBetween(Float value1, Float value2) {
            addCriterion("comment_service not between", value1, value2, "commentService");
            return (Criteria) this;
        }

        public Criteria andCommentCourierIsNull() {
            addCriterion("comment_courier is null");
            return (Criteria) this;
        }

        public Criteria andCommentCourierIsNotNull() {
            addCriterion("comment_courier is not null");
            return (Criteria) this;
        }

        public Criteria andCommentCourierEqualTo(Float value) {
            addCriterion("comment_courier =", value, "commentCourier");
            return (Criteria) this;
        }

        public Criteria andCommentCourierNotEqualTo(Float value) {
            addCriterion("comment_courier <>", value, "commentCourier");
            return (Criteria) this;
        }

        public Criteria andCommentCourierGreaterThan(Float value) {
            addCriterion("comment_courier >", value, "commentCourier");
            return (Criteria) this;
        }

        public Criteria andCommentCourierGreaterThanOrEqualTo(Float value) {
            addCriterion("comment_courier >=", value, "commentCourier");
            return (Criteria) this;
        }

        public Criteria andCommentCourierLessThan(Float value) {
            addCriterion("comment_courier <", value, "commentCourier");
            return (Criteria) this;
        }

        public Criteria andCommentCourierLessThanOrEqualTo(Float value) {
            addCriterion("comment_courier <=", value, "commentCourier");
            return (Criteria) this;
        }

        public Criteria andCommentCourierIn(List<Float> values) {
            addCriterion("comment_courier in", values, "commentCourier");
            return (Criteria) this;
        }

        public Criteria andCommentCourierNotIn(List<Float> values) {
            addCriterion("comment_courier not in", values, "commentCourier");
            return (Criteria) this;
        }

        public Criteria andCommentCourierBetween(Float value1, Float value2) {
            addCriterion("comment_courier between", value1, value2, "commentCourier");
            return (Criteria) this;
        }

        public Criteria andCommentCourierNotBetween(Float value1, Float value2) {
            addCriterion("comment_courier not between", value1, value2, "commentCourier");
            return (Criteria) this;
        }

        public Criteria andCommentTimeIsNull() {
            addCriterion("comment_time is null");
            return (Criteria) this;
        }

        public Criteria andCommentTimeIsNotNull() {
            addCriterion("comment_time is not null");
            return (Criteria) this;
        }

        public Criteria andCommentTimeEqualTo(Integer value) {
            addCriterion("comment_time =", value, "commentTime");
            return (Criteria) this;
        }

        public Criteria andCommentTimeNotEqualTo(Integer value) {
            addCriterion("comment_time <>", value, "commentTime");
            return (Criteria) this;
        }

        public Criteria andCommentTimeGreaterThan(Integer value) {
            addCriterion("comment_time >", value, "commentTime");
            return (Criteria) this;
        }

        public Criteria andCommentTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("comment_time >=", value, "commentTime");
            return (Criteria) this;
        }

        public Criteria andCommentTimeLessThan(Integer value) {
            addCriterion("comment_time <", value, "commentTime");
            return (Criteria) this;
        }

        public Criteria andCommentTimeLessThanOrEqualTo(Integer value) {
            addCriterion("comment_time <=", value, "commentTime");
            return (Criteria) this;
        }

        public Criteria andCommentTimeIn(List<Integer> values) {
            addCriterion("comment_time in", values, "commentTime");
            return (Criteria) this;
        }

        public Criteria andCommentTimeNotIn(List<Integer> values) {
            addCriterion("comment_time not in", values, "commentTime");
            return (Criteria) this;
        }

        public Criteria andCommentTimeBetween(Integer value1, Integer value2) {
            addCriterion("comment_time between", value1, value2, "commentTime");
            return (Criteria) this;
        }

        public Criteria andCommentTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("comment_time not between", value1, value2, "commentTime");
            return (Criteria) this;
        }

        public Criteria andMerchantIdIsNull() {
            addCriterion("merchant_id is null");
            return (Criteria) this;
        }

        public Criteria andMerchantIdIsNotNull() {
            addCriterion("merchant_id is not null");
            return (Criteria) this;
        }

        public Criteria andMerchantIdEqualTo(Integer value) {
            addCriterion("merchant_id =", value, "merchantId");
            return (Criteria) this;
        }

        public Criteria andMerchantIdNotEqualTo(Integer value) {
            addCriterion("merchant_id <>", value, "merchantId");
            return (Criteria) this;
        }

        public Criteria andMerchantIdGreaterThan(Integer value) {
            addCriterion("merchant_id >", value, "merchantId");
            return (Criteria) this;
        }

        public Criteria andMerchantIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("merchant_id >=", value, "merchantId");
            return (Criteria) this;
        }

        public Criteria andMerchantIdLessThan(Integer value) {
            addCriterion("merchant_id <", value, "merchantId");
            return (Criteria) this;
        }

        public Criteria andMerchantIdLessThanOrEqualTo(Integer value) {
            addCriterion("merchant_id <=", value, "merchantId");
            return (Criteria) this;
        }

        public Criteria andMerchantIdIn(List<Integer> values) {
            addCriterion("merchant_id in", values, "merchantId");
            return (Criteria) this;
        }

        public Criteria andMerchantIdNotIn(List<Integer> values) {
            addCriterion("merchant_id not in", values, "merchantId");
            return (Criteria) this;
        }

        public Criteria andMerchantIdBetween(Integer value1, Integer value2) {
            addCriterion("merchant_id between", value1, value2, "merchantId");
            return (Criteria) this;
        }

        public Criteria andMerchantIdNotBetween(Integer value1, Integer value2) {
            addCriterion("merchant_id not between", value1, value2, "merchantId");
            return (Criteria) this;
        }

        public Criteria andScoreMoneyIsNull() {
            addCriterion("score_money is null");
            return (Criteria) this;
        }

        public Criteria andScoreMoneyIsNotNull() {
            addCriterion("score_money is not null");
            return (Criteria) this;
        }

        public Criteria andScoreMoneyEqualTo(Double value) {
            addCriterion("score_money =", value, "scoreMoney");
            return (Criteria) this;
        }

        public Criteria andScoreMoneyNotEqualTo(Double value) {
            addCriterion("score_money <>", value, "scoreMoney");
            return (Criteria) this;
        }

        public Criteria andScoreMoneyGreaterThan(Double value) {
            addCriterion("score_money >", value, "scoreMoney");
            return (Criteria) this;
        }

        public Criteria andScoreMoneyGreaterThanOrEqualTo(Double value) {
            addCriterion("score_money >=", value, "scoreMoney");
            return (Criteria) this;
        }

        public Criteria andScoreMoneyLessThan(Double value) {
            addCriterion("score_money <", value, "scoreMoney");
            return (Criteria) this;
        }

        public Criteria andScoreMoneyLessThanOrEqualTo(Double value) {
            addCriterion("score_money <=", value, "scoreMoney");
            return (Criteria) this;
        }

        public Criteria andScoreMoneyIn(List<Double> values) {
            addCriterion("score_money in", values, "scoreMoney");
            return (Criteria) this;
        }

        public Criteria andScoreMoneyNotIn(List<Double> values) {
            addCriterion("score_money not in", values, "scoreMoney");
            return (Criteria) this;
        }

        public Criteria andScoreMoneyBetween(Double value1, Double value2) {
            addCriterion("score_money between", value1, value2, "scoreMoney");
            return (Criteria) this;
        }

        public Criteria andScoreMoneyNotBetween(Double value1, Double value2) {
            addCriterion("score_money not between", value1, value2, "scoreMoney");
            return (Criteria) this;
        }

        public Criteria andScoreIsNull() {
            addCriterion("score is null");
            return (Criteria) this;
        }

        public Criteria andScoreIsNotNull() {
            addCriterion("score is not null");
            return (Criteria) this;
        }

        public Criteria andScoreEqualTo(Integer value) {
            addCriterion("score =", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreNotEqualTo(Integer value) {
            addCriterion("score <>", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreGreaterThan(Integer value) {
            addCriterion("score >", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreGreaterThanOrEqualTo(Integer value) {
            addCriterion("score >=", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreLessThan(Integer value) {
            addCriterion("score <", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreLessThanOrEqualTo(Integer value) {
            addCriterion("score <=", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreIn(List<Integer> values) {
            addCriterion("score in", values, "score");
            return (Criteria) this;
        }

        public Criteria andScoreNotIn(List<Integer> values) {
            addCriterion("score not in", values, "score");
            return (Criteria) this;
        }

        public Criteria andScoreBetween(Integer value1, Integer value2) {
            addCriterion("score between", value1, value2, "score");
            return (Criteria) this;
        }

        public Criteria andScoreNotBetween(Integer value1, Integer value2) {
            addCriterion("score not between", value1, value2, "score");
            return (Criteria) this;
        }

        public Criteria andOrderTypeIsNull() {
            addCriterion("order_type is null");
            return (Criteria) this;
        }

        public Criteria andOrderTypeIsNotNull() {
            addCriterion("order_type is not null");
            return (Criteria) this;
        }

        public Criteria andOrderTypeEqualTo(String value) {
            addCriterion("order_type =", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeNotEqualTo(String value) {
            addCriterion("order_type <>", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeGreaterThan(String value) {
            addCriterion("order_type >", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeGreaterThanOrEqualTo(String value) {
            addCriterion("order_type >=", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeLessThan(String value) {
            addCriterion("order_type <", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeLessThanOrEqualTo(String value) {
            addCriterion("order_type <=", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeLike(String value) {
            addCriterion("order_type like", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeNotLike(String value) {
            addCriterion("order_type not like", value, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeIn(List<String> values) {
            addCriterion("order_type in", values, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeNotIn(List<String> values) {
            addCriterion("order_type not in", values, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeBetween(String value1, String value2) {
            addCriterion("order_type between", value1, value2, "orderType");
            return (Criteria) this;
        }

        public Criteria andOrderTypeNotBetween(String value1, String value2) {
            addCriterion("order_type not between", value1, value2, "orderType");
            return (Criteria) this;
        }

        public Criteria andAccessTimeIsNull() {
            addCriterion("access_time is null");
            return (Criteria) this;
        }

        public Criteria andAccessTimeIsNotNull() {
            addCriterion("access_time is not null");
            return (Criteria) this;
        }

        public Criteria andAccessTimeEqualTo(Integer value) {
            addCriterion("access_time =", value, "accessTime");
            return (Criteria) this;
        }

        public Criteria andAccessTimeNotEqualTo(Integer value) {
            addCriterion("access_time <>", value, "accessTime");
            return (Criteria) this;
        }

        public Criteria andAccessTimeGreaterThan(Integer value) {
            addCriterion("access_time >", value, "accessTime");
            return (Criteria) this;
        }

        public Criteria andAccessTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("access_time >=", value, "accessTime");
            return (Criteria) this;
        }

        public Criteria andAccessTimeLessThan(Integer value) {
            addCriterion("access_time <", value, "accessTime");
            return (Criteria) this;
        }

        public Criteria andAccessTimeLessThanOrEqualTo(Integer value) {
            addCriterion("access_time <=", value, "accessTime");
            return (Criteria) this;
        }

        public Criteria andAccessTimeIn(List<Integer> values) {
            addCriterion("access_time in", values, "accessTime");
            return (Criteria) this;
        }

        public Criteria andAccessTimeNotIn(List<Integer> values) {
            addCriterion("access_time not in", values, "accessTime");
            return (Criteria) this;
        }

        public Criteria andAccessTimeBetween(Integer value1, Integer value2) {
            addCriterion("access_time between", value1, value2, "accessTime");
            return (Criteria) this;
        }

        public Criteria andAccessTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("access_time not between", value1, value2, "accessTime");
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

        public Criteria andCompleteTimeIsNull() {
            addCriterion("complete_time is null");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeIsNotNull() {
            addCriterion("complete_time is not null");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeEqualTo(Integer value) {
            addCriterion("complete_time =", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeNotEqualTo(Integer value) {
            addCriterion("complete_time <>", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeGreaterThan(Integer value) {
            addCriterion("complete_time >", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("complete_time >=", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeLessThan(Integer value) {
            addCriterion("complete_time <", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeLessThanOrEqualTo(Integer value) {
            addCriterion("complete_time <=", value, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeIn(List<Integer> values) {
            addCriterion("complete_time in", values, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeNotIn(List<Integer> values) {
            addCriterion("complete_time not in", values, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeBetween(Integer value1, Integer value2) {
            addCriterion("complete_time between", value1, value2, "completeTime");
            return (Criteria) this;
        }

        public Criteria andCompleteTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("complete_time not between", value1, value2, "completeTime");
            return (Criteria) this;
        }

        public Criteria andUrgentTimeIsNull() {
            addCriterion("urgent_time is null");
            return (Criteria) this;
        }

        public Criteria andUrgentTimeIsNotNull() {
            addCriterion("urgent_time is not null");
            return (Criteria) this;
        }

        public Criteria andUrgentTimeEqualTo(Integer value) {
            addCriterion("urgent_time =", value, "urgentTime");
            return (Criteria) this;
        }

        public Criteria andUrgentTimeNotEqualTo(Integer value) {
            addCriterion("urgent_time <>", value, "urgentTime");
            return (Criteria) this;
        }

        public Criteria andUrgentTimeGreaterThan(Integer value) {
            addCriterion("urgent_time >", value, "urgentTime");
            return (Criteria) this;
        }

        public Criteria andUrgentTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("urgent_time >=", value, "urgentTime");
            return (Criteria) this;
        }

        public Criteria andUrgentTimeLessThan(Integer value) {
            addCriterion("urgent_time <", value, "urgentTime");
            return (Criteria) this;
        }

        public Criteria andUrgentTimeLessThanOrEqualTo(Integer value) {
            addCriterion("urgent_time <=", value, "urgentTime");
            return (Criteria) this;
        }

        public Criteria andUrgentTimeIn(List<Integer> values) {
            addCriterion("urgent_time in", values, "urgentTime");
            return (Criteria) this;
        }

        public Criteria andUrgentTimeNotIn(List<Integer> values) {
            addCriterion("urgent_time not in", values, "urgentTime");
            return (Criteria) this;
        }

        public Criteria andUrgentTimeBetween(Integer value1, Integer value2) {
            addCriterion("urgent_time between", value1, value2, "urgentTime");
            return (Criteria) this;
        }

        public Criteria andUrgentTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("urgent_time not between", value1, value2, "urgentTime");
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

        public Criteria andIfcourierIsNull() {
            addCriterion("ifCourier is null");
            return (Criteria) this;
        }

        public Criteria andIfcourierIsNotNull() {
            addCriterion("ifCourier is not null");
            return (Criteria) this;
        }

        public Criteria andIfcourierEqualTo(String value) {
            addCriterion("ifCourier =", value, "ifcourier");
            return (Criteria) this;
        }

        public Criteria andIfcourierNotEqualTo(String value) {
            addCriterion("ifCourier <>", value, "ifcourier");
            return (Criteria) this;
        }

        public Criteria andIfcourierGreaterThan(String value) {
            addCriterion("ifCourier >", value, "ifcourier");
            return (Criteria) this;
        }

        public Criteria andIfcourierGreaterThanOrEqualTo(String value) {
            addCriterion("ifCourier >=", value, "ifcourier");
            return (Criteria) this;
        }

        public Criteria andIfcourierLessThan(String value) {
            addCriterion("ifCourier <", value, "ifcourier");
            return (Criteria) this;
        }

        public Criteria andIfcourierLessThanOrEqualTo(String value) {
            addCriterion("ifCourier <=", value, "ifcourier");
            return (Criteria) this;
        }

        public Criteria andIfcourierLike(String value) {
            addCriterion("ifCourier like", value, "ifcourier");
            return (Criteria) this;
        }

        public Criteria andIfcourierNotLike(String value) {
            addCriterion("ifCourier not like", value, "ifcourier");
            return (Criteria) this;
        }

        public Criteria andIfcourierIn(List<String> values) {
            addCriterion("ifCourier in", values, "ifcourier");
            return (Criteria) this;
        }

        public Criteria andIfcourierNotIn(List<String> values) {
            addCriterion("ifCourier not in", values, "ifcourier");
            return (Criteria) this;
        }

        public Criteria andIfcourierBetween(String value1, String value2) {
            addCriterion("ifCourier between", value1, value2, "ifcourier");
            return (Criteria) this;
        }

        public Criteria andIfcourierNotBetween(String value1, String value2) {
            addCriterion("ifCourier not between", value1, value2, "ifcourier");
            return (Criteria) this;
        }

        public Criteria andDeliveryDoneTimeIsNull() {
            addCriterion("delivery_done_time is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryDoneTimeIsNotNull() {
            addCriterion("delivery_done_time is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryDoneTimeEqualTo(Integer value) {
            addCriterion("delivery_done_time =", value, "deliveryDoneTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryDoneTimeNotEqualTo(Integer value) {
            addCriterion("delivery_done_time <>", value, "deliveryDoneTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryDoneTimeGreaterThan(Integer value) {
            addCriterion("delivery_done_time >", value, "deliveryDoneTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryDoneTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("delivery_done_time >=", value, "deliveryDoneTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryDoneTimeLessThan(Integer value) {
            addCriterion("delivery_done_time <", value, "deliveryDoneTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryDoneTimeLessThanOrEqualTo(Integer value) {
            addCriterion("delivery_done_time <=", value, "deliveryDoneTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryDoneTimeIn(List<Integer> values) {
            addCriterion("delivery_done_time in", values, "deliveryDoneTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryDoneTimeNotIn(List<Integer> values) {
            addCriterion("delivery_done_time not in", values, "deliveryDoneTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryDoneTimeBetween(Integer value1, Integer value2) {
            addCriterion("delivery_done_time between", value1, value2, "deliveryDoneTime");
            return (Criteria) this;
        }

        public Criteria andDeliveryDoneTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("delivery_done_time not between", value1, value2, "deliveryDoneTime");
            return (Criteria) this;
        }

        public Criteria andPayStateIsNull() {
            addCriterion("pay_state is null");
            return (Criteria) this;
        }

        public Criteria andPayStateIsNotNull() {
            addCriterion("pay_state is not null");
            return (Criteria) this;
        }

        public Criteria andPayStateEqualTo(String value) {
            addCriterion("pay_state =", value, "payState");
            return (Criteria) this;
        }

        public Criteria andPayStateNotEqualTo(String value) {
            addCriterion("pay_state <>", value, "payState");
            return (Criteria) this;
        }

        public Criteria andPayStateGreaterThan(String value) {
            addCriterion("pay_state >", value, "payState");
            return (Criteria) this;
        }

        public Criteria andPayStateGreaterThanOrEqualTo(String value) {
            addCriterion("pay_state >=", value, "payState");
            return (Criteria) this;
        }

        public Criteria andPayStateLessThan(String value) {
            addCriterion("pay_state <", value, "payState");
            return (Criteria) this;
        }

        public Criteria andPayStateLessThanOrEqualTo(String value) {
            addCriterion("pay_state <=", value, "payState");
            return (Criteria) this;
        }

        public Criteria andPayStateLike(String value) {
            addCriterion("pay_state like", value, "payState");
            return (Criteria) this;
        }

        public Criteria andPayStateNotLike(String value) {
            addCriterion("pay_state not like", value, "payState");
            return (Criteria) this;
        }

        public Criteria andPayStateIn(List<String> values) {
            addCriterion("pay_state in", values, "payState");
            return (Criteria) this;
        }

        public Criteria andPayStateNotIn(List<String> values) {
            addCriterion("pay_state not in", values, "payState");
            return (Criteria) this;
        }

        public Criteria andPayStateBetween(String value1, String value2) {
            addCriterion("pay_state between", value1, value2, "payState");
            return (Criteria) this;
        }

        public Criteria andPayStateNotBetween(String value1, String value2) {
            addCriterion("pay_state not between", value1, value2, "payState");
            return (Criteria) this;
        }

        public Criteria andSaleTypeIsNull() {
            addCriterion("sale_type is null");
            return (Criteria) this;
        }

        public Criteria andSaleTypeIsNotNull() {
            addCriterion("sale_type is not null");
            return (Criteria) this;
        }

        public Criteria andSaleTypeEqualTo(Integer value) {
            addCriterion("sale_type =", value, "saleType");
            return (Criteria) this;
        }

        public Criteria andSaleTypeNotEqualTo(Integer value) {
            addCriterion("sale_type <>", value, "saleType");
            return (Criteria) this;
        }

        public Criteria andSaleTypeGreaterThan(Integer value) {
            addCriterion("sale_type >", value, "saleType");
            return (Criteria) this;
        }

        public Criteria andSaleTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("sale_type >=", value, "saleType");
            return (Criteria) this;
        }

        public Criteria andSaleTypeLessThan(Integer value) {
            addCriterion("sale_type <", value, "saleType");
            return (Criteria) this;
        }

        public Criteria andSaleTypeLessThanOrEqualTo(Integer value) {
            addCriterion("sale_type <=", value, "saleType");
            return (Criteria) this;
        }

        public Criteria andSaleTypeIn(List<Integer> values) {
            addCriterion("sale_type in", values, "saleType");
            return (Criteria) this;
        }

        public Criteria andSaleTypeNotIn(List<Integer> values) {
            addCriterion("sale_type not in", values, "saleType");
            return (Criteria) this;
        }

        public Criteria andSaleTypeBetween(Integer value1, Integer value2) {
            addCriterion("sale_type between", value1, value2, "saleType");
            return (Criteria) this;
        }

        public Criteria andSaleTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("sale_type not between", value1, value2, "saleType");
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

        public Criteria andOrderNumEqualTo(String value) {
            addCriterion("order_num =", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumNotEqualTo(String value) {
            addCriterion("order_num <>", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumGreaterThan(String value) {
            addCriterion("order_num >", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumGreaterThanOrEqualTo(String value) {
            addCriterion("order_num >=", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumLessThan(String value) {
            addCriterion("order_num <", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumLessThanOrEqualTo(String value) {
            addCriterion("order_num <=", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumLike(String value) {
            addCriterion("order_num like", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumNotLike(String value) {
            addCriterion("order_num not like", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumIn(List<String> values) {
            addCriterion("order_num in", values, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumNotIn(List<String> values) {
            addCriterion("order_num not in", values, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumBetween(String value1, String value2) {
            addCriterion("order_num between", value1, value2, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumNotBetween(String value1, String value2) {
            addCriterion("order_num not between", value1, value2, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOutTraceIdIsNull() {
            addCriterion("out_trace_id is null");
            return (Criteria) this;
        }

        public Criteria andOutTraceIdIsNotNull() {
            addCriterion("out_trace_id is not null");
            return (Criteria) this;
        }

        public Criteria andOutTraceIdEqualTo(String value) {
            addCriterion("out_trace_id =", value, "outTraceId");
            return (Criteria) this;
        }

        public Criteria andOutTraceIdNotEqualTo(String value) {
            addCriterion("out_trace_id <>", value, "outTraceId");
            return (Criteria) this;
        }

        public Criteria andOutTraceIdGreaterThan(String value) {
            addCriterion("out_trace_id >", value, "outTraceId");
            return (Criteria) this;
        }

        public Criteria andOutTraceIdGreaterThanOrEqualTo(String value) {
            addCriterion("out_trace_id >=", value, "outTraceId");
            return (Criteria) this;
        }

        public Criteria andOutTraceIdLessThan(String value) {
            addCriterion("out_trace_id <", value, "outTraceId");
            return (Criteria) this;
        }

        public Criteria andOutTraceIdLessThanOrEqualTo(String value) {
            addCriterion("out_trace_id <=", value, "outTraceId");
            return (Criteria) this;
        }

        public Criteria andOutTraceIdLike(String value) {
            addCriterion("out_trace_id like", value, "outTraceId");
            return (Criteria) this;
        }

        public Criteria andOutTraceIdNotLike(String value) {
            addCriterion("out_trace_id not like", value, "outTraceId");
            return (Criteria) this;
        }

        public Criteria andOutTraceIdIn(List<String> values) {
            addCriterion("out_trace_id in", values, "outTraceId");
            return (Criteria) this;
        }

        public Criteria andOutTraceIdNotIn(List<String> values) {
            addCriterion("out_trace_id not in", values, "outTraceId");
            return (Criteria) this;
        }

        public Criteria andOutTraceIdBetween(String value1, String value2) {
            addCriterion("out_trace_id between", value1, value2, "outTraceId");
            return (Criteria) this;
        }

        public Criteria andOutTraceIdNotBetween(String value1, String value2) {
            addCriterion("out_trace_id not between", value1, value2, "outTraceId");
            return (Criteria) this;
        }

        public Criteria andTimeRemarkIsNull() {
            addCriterion("time_remark is null");
            return (Criteria) this;
        }

        public Criteria andTimeRemarkIsNotNull() {
            addCriterion("time_remark is not null");
            return (Criteria) this;
        }

        public Criteria andTimeRemarkEqualTo(String value) {
            addCriterion("time_remark =", value, "timeRemark");
            return (Criteria) this;
        }

        public Criteria andTimeRemarkNotEqualTo(String value) {
            addCriterion("time_remark <>", value, "timeRemark");
            return (Criteria) this;
        }

        public Criteria andTimeRemarkGreaterThan(String value) {
            addCriterion("time_remark >", value, "timeRemark");
            return (Criteria) this;
        }

        public Criteria andTimeRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("time_remark >=", value, "timeRemark");
            return (Criteria) this;
        }

        public Criteria andTimeRemarkLessThan(String value) {
            addCriterion("time_remark <", value, "timeRemark");
            return (Criteria) this;
        }

        public Criteria andTimeRemarkLessThanOrEqualTo(String value) {
            addCriterion("time_remark <=", value, "timeRemark");
            return (Criteria) this;
        }

        public Criteria andTimeRemarkLike(String value) {
            addCriterion("time_remark like", value, "timeRemark");
            return (Criteria) this;
        }

        public Criteria andTimeRemarkNotLike(String value) {
            addCriterion("time_remark not like", value, "timeRemark");
            return (Criteria) this;
        }

        public Criteria andTimeRemarkIn(List<String> values) {
            addCriterion("time_remark in", values, "timeRemark");
            return (Criteria) this;
        }

        public Criteria andTimeRemarkNotIn(List<String> values) {
            addCriterion("time_remark not in", values, "timeRemark");
            return (Criteria) this;
        }

        public Criteria andTimeRemarkBetween(String value1, String value2) {
            addCriterion("time_remark between", value1, value2, "timeRemark");
            return (Criteria) this;
        }

        public Criteria andTimeRemarkNotBetween(String value1, String value2) {
            addCriterion("time_remark not between", value1, value2, "timeRemark");
            return (Criteria) this;
        }

        public Criteria andCookDoneTimeIsNull() {
            addCriterion("cook_done_time is null");
            return (Criteria) this;
        }

        public Criteria andCookDoneTimeIsNotNull() {
            addCriterion("cook_done_time is not null");
            return (Criteria) this;
        }

        public Criteria andCookDoneTimeEqualTo(Integer value) {
            addCriterion("cook_done_time =", value, "cookDoneTime");
            return (Criteria) this;
        }

        public Criteria andCookDoneTimeNotEqualTo(Integer value) {
            addCriterion("cook_done_time <>", value, "cookDoneTime");
            return (Criteria) this;
        }

        public Criteria andCookDoneTimeGreaterThan(Integer value) {
            addCriterion("cook_done_time >", value, "cookDoneTime");
            return (Criteria) this;
        }

        public Criteria andCookDoneTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("cook_done_time >=", value, "cookDoneTime");
            return (Criteria) this;
        }

        public Criteria andCookDoneTimeLessThan(Integer value) {
            addCriterion("cook_done_time <", value, "cookDoneTime");
            return (Criteria) this;
        }

        public Criteria andCookDoneTimeLessThanOrEqualTo(Integer value) {
            addCriterion("cook_done_time <=", value, "cookDoneTime");
            return (Criteria) this;
        }

        public Criteria andCookDoneTimeIn(List<Integer> values) {
            addCriterion("cook_done_time in", values, "cookDoneTime");
            return (Criteria) this;
        }

        public Criteria andCookDoneTimeNotIn(List<Integer> values) {
            addCriterion("cook_done_time not in", values, "cookDoneTime");
            return (Criteria) this;
        }

        public Criteria andCookDoneTimeBetween(Integer value1, Integer value2) {
            addCriterion("cook_done_time between", value1, value2, "cookDoneTime");
            return (Criteria) this;
        }

        public Criteria andCookDoneTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("cook_done_time not between", value1, value2, "cookDoneTime");
            return (Criteria) this;
        }

        public Criteria andCookDoneCodeIsNull() {
            addCriterion("cook_done_code is null");
            return (Criteria) this;
        }

        public Criteria andCookDoneCodeIsNotNull() {
            addCriterion("cook_done_code is not null");
            return (Criteria) this;
        }

        public Criteria andCookDoneCodeEqualTo(String value) {
            addCriterion("cook_done_code =", value, "cookDoneCode");
            return (Criteria) this;
        }

        public Criteria andCookDoneCodeNotEqualTo(String value) {
            addCriterion("cook_done_code <>", value, "cookDoneCode");
            return (Criteria) this;
        }

        public Criteria andCookDoneCodeGreaterThan(String value) {
            addCriterion("cook_done_code >", value, "cookDoneCode");
            return (Criteria) this;
        }

        public Criteria andCookDoneCodeGreaterThanOrEqualTo(String value) {
            addCriterion("cook_done_code >=", value, "cookDoneCode");
            return (Criteria) this;
        }

        public Criteria andCookDoneCodeLessThan(String value) {
            addCriterion("cook_done_code <", value, "cookDoneCode");
            return (Criteria) this;
        }

        public Criteria andCookDoneCodeLessThanOrEqualTo(String value) {
            addCriterion("cook_done_code <=", value, "cookDoneCode");
            return (Criteria) this;
        }

        public Criteria andCookDoneCodeLike(String value) {
            addCriterion("cook_done_code like", value, "cookDoneCode");
            return (Criteria) this;
        }

        public Criteria andCookDoneCodeNotLike(String value) {
            addCriterion("cook_done_code not like", value, "cookDoneCode");
            return (Criteria) this;
        }

        public Criteria andCookDoneCodeIn(List<String> values) {
            addCriterion("cook_done_code in", values, "cookDoneCode");
            return (Criteria) this;
        }

        public Criteria andCookDoneCodeNotIn(List<String> values) {
            addCriterion("cook_done_code not in", values, "cookDoneCode");
            return (Criteria) this;
        }

        public Criteria andCookDoneCodeBetween(String value1, String value2) {
            addCriterion("cook_done_code between", value1, value2, "cookDoneCode");
            return (Criteria) this;
        }

        public Criteria andCookDoneCodeNotBetween(String value1, String value2) {
            addCriterion("cook_done_code not between", value1, value2, "cookDoneCode");
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

        public Criteria andCommentCourierContentIsNull() {
            addCriterion("comment_courier_content is null");
            return (Criteria) this;
        }

        public Criteria andCommentCourierContentIsNotNull() {
            addCriterion("comment_courier_content is not null");
            return (Criteria) this;
        }

        public Criteria andCommentCourierContentEqualTo(String value) {
            addCriterion("comment_courier_content =", value, "commentCourierContent");
            return (Criteria) this;
        }

        public Criteria andCommentCourierContentNotEqualTo(String value) {
            addCriterion("comment_courier_content <>", value, "commentCourierContent");
            return (Criteria) this;
        }

        public Criteria andCommentCourierContentGreaterThan(String value) {
            addCriterion("comment_courier_content >", value, "commentCourierContent");
            return (Criteria) this;
        }

        public Criteria andCommentCourierContentGreaterThanOrEqualTo(String value) {
            addCriterion("comment_courier_content >=", value, "commentCourierContent");
            return (Criteria) this;
        }

        public Criteria andCommentCourierContentLessThan(String value) {
            addCriterion("comment_courier_content <", value, "commentCourierContent");
            return (Criteria) this;
        }

        public Criteria andCommentCourierContentLessThanOrEqualTo(String value) {
            addCriterion("comment_courier_content <=", value, "commentCourierContent");
            return (Criteria) this;
        }

        public Criteria andCommentCourierContentLike(String value) {
            addCriterion("comment_courier_content like", value, "commentCourierContent");
            return (Criteria) this;
        }

        public Criteria andCommentCourierContentNotLike(String value) {
            addCriterion("comment_courier_content not like", value, "commentCourierContent");
            return (Criteria) this;
        }

        public Criteria andCommentCourierContentIn(List<String> values) {
            addCriterion("comment_courier_content in", values, "commentCourierContent");
            return (Criteria) this;
        }

        public Criteria andCommentCourierContentNotIn(List<String> values) {
            addCriterion("comment_courier_content not in", values, "commentCourierContent");
            return (Criteria) this;
        }

        public Criteria andCommentCourierContentBetween(String value1, String value2) {
            addCriterion("comment_courier_content between", value1, value2, "commentCourierContent");
            return (Criteria) this;
        }

        public Criteria andCommentCourierContentNotBetween(String value1, String value2) {
            addCriterion("comment_courier_content not between", value1, value2, "commentCourierContent");
            return (Criteria) this;
        }

        public Criteria andStartSendTimeIsNull() {
            addCriterion("start_send_time is null");
            return (Criteria) this;
        }

        public Criteria andStartSendTimeIsNotNull() {
            addCriterion("start_send_time is not null");
            return (Criteria) this;
        }

        public Criteria andStartSendTimeEqualTo(Integer value) {
            addCriterion("start_send_time =", value, "startSendTime");
            return (Criteria) this;
        }

        public Criteria andStartSendTimeNotEqualTo(Integer value) {
            addCriterion("start_send_time <>", value, "startSendTime");
            return (Criteria) this;
        }

        public Criteria andStartSendTimeGreaterThan(Integer value) {
            addCriterion("start_send_time >", value, "startSendTime");
            return (Criteria) this;
        }

        public Criteria andStartSendTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("start_send_time >=", value, "startSendTime");
            return (Criteria) this;
        }

        public Criteria andStartSendTimeLessThan(Integer value) {
            addCriterion("start_send_time <", value, "startSendTime");
            return (Criteria) this;
        }

        public Criteria andStartSendTimeLessThanOrEqualTo(Integer value) {
            addCriterion("start_send_time <=", value, "startSendTime");
            return (Criteria) this;
        }

        public Criteria andStartSendTimeIn(List<Integer> values) {
            addCriterion("start_send_time in", values, "startSendTime");
            return (Criteria) this;
        }

        public Criteria andStartSendTimeNotIn(List<Integer> values) {
            addCriterion("start_send_time not in", values, "startSendTime");
            return (Criteria) this;
        }

        public Criteria andStartSendTimeBetween(Integer value1, Integer value2) {
            addCriterion("start_send_time between", value1, value2, "startSendTime");
            return (Criteria) this;
        }

        public Criteria andStartSendTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("start_send_time not between", value1, value2, "startSendTime");
            return (Criteria) this;
        }

        public Criteria andEndSendTimeIsNull() {
            addCriterion("end_send_time is null");
            return (Criteria) this;
        }

        public Criteria andEndSendTimeIsNotNull() {
            addCriterion("end_send_time is not null");
            return (Criteria) this;
        }

        public Criteria andEndSendTimeEqualTo(Integer value) {
            addCriterion("end_send_time =", value, "endSendTime");
            return (Criteria) this;
        }

        public Criteria andEndSendTimeNotEqualTo(Integer value) {
            addCriterion("end_send_time <>", value, "endSendTime");
            return (Criteria) this;
        }

        public Criteria andEndSendTimeGreaterThan(Integer value) {
            addCriterion("end_send_time >", value, "endSendTime");
            return (Criteria) this;
        }

        public Criteria andEndSendTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("end_send_time >=", value, "endSendTime");
            return (Criteria) this;
        }

        public Criteria andEndSendTimeLessThan(Integer value) {
            addCriterion("end_send_time <", value, "endSendTime");
            return (Criteria) this;
        }

        public Criteria andEndSendTimeLessThanOrEqualTo(Integer value) {
            addCriterion("end_send_time <=", value, "endSendTime");
            return (Criteria) this;
        }

        public Criteria andEndSendTimeIn(List<Integer> values) {
            addCriterion("end_send_time in", values, "endSendTime");
            return (Criteria) this;
        }

        public Criteria andEndSendTimeNotIn(List<Integer> values) {
            addCriterion("end_send_time not in", values, "endSendTime");
            return (Criteria) this;
        }

        public Criteria andEndSendTimeBetween(Integer value1, Integer value2) {
            addCriterion("end_send_time between", value1, value2, "endSendTime");
            return (Criteria) this;
        }

        public Criteria andEndSendTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("end_send_time not between", value1, value2, "endSendTime");
            return (Criteria) this;
        }

        public Criteria andUserAddressIdIsNull() {
            addCriterion("user_address_id is null");
            return (Criteria) this;
        }

        public Criteria andUserAddressIdIsNotNull() {
            addCriterion("user_address_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserAddressIdEqualTo(Integer value) {
            addCriterion("user_address_id =", value, "userAddressId");
            return (Criteria) this;
        }

        public Criteria andUserAddressIdNotEqualTo(Integer value) {
            addCriterion("user_address_id <>", value, "userAddressId");
            return (Criteria) this;
        }

        public Criteria andUserAddressIdGreaterThan(Integer value) {
            addCriterion("user_address_id >", value, "userAddressId");
            return (Criteria) this;
        }

        public Criteria andUserAddressIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_address_id >=", value, "userAddressId");
            return (Criteria) this;
        }

        public Criteria andUserAddressIdLessThan(Integer value) {
            addCriterion("user_address_id <", value, "userAddressId");
            return (Criteria) this;
        }

        public Criteria andUserAddressIdLessThanOrEqualTo(Integer value) {
            addCriterion("user_address_id <=", value, "userAddressId");
            return (Criteria) this;
        }

        public Criteria andUserAddressIdIn(List<Integer> values) {
            addCriterion("user_address_id in", values, "userAddressId");
            return (Criteria) this;
        }

        public Criteria andUserAddressIdNotIn(List<Integer> values) {
            addCriterion("user_address_id not in", values, "userAddressId");
            return (Criteria) this;
        }

        public Criteria andUserAddressIdBetween(Integer value1, Integer value2) {
            addCriterion("user_address_id between", value1, value2, "userAddressId");
            return (Criteria) this;
        }

        public Criteria andUserAddressIdNotBetween(Integer value1, Integer value2) {
            addCriterion("user_address_id not between", value1, value2, "userAddressId");
            return (Criteria) this;
        }

        public Criteria andInvoiceIsNull() {
            addCriterion("invoice is null");
            return (Criteria) this;
        }

        public Criteria andInvoiceIsNotNull() {
            addCriterion("invoice is not null");
            return (Criteria) this;
        }

        public Criteria andInvoiceEqualTo(String value) {
            addCriterion("invoice =", value, "invoice");
            return (Criteria) this;
        }

        public Criteria andInvoiceNotEqualTo(String value) {
            addCriterion("invoice <>", value, "invoice");
            return (Criteria) this;
        }

        public Criteria andInvoiceGreaterThan(String value) {
            addCriterion("invoice >", value, "invoice");
            return (Criteria) this;
        }

        public Criteria andInvoiceGreaterThanOrEqualTo(String value) {
            addCriterion("invoice >=", value, "invoice");
            return (Criteria) this;
        }

        public Criteria andInvoiceLessThan(String value) {
            addCriterion("invoice <", value, "invoice");
            return (Criteria) this;
        }

        public Criteria andInvoiceLessThanOrEqualTo(String value) {
            addCriterion("invoice <=", value, "invoice");
            return (Criteria) this;
        }

        public Criteria andInvoiceLike(String value) {
            addCriterion("invoice like", value, "invoice");
            return (Criteria) this;
        }

        public Criteria andInvoiceNotLike(String value) {
            addCriterion("invoice not like", value, "invoice");
            return (Criteria) this;
        }

        public Criteria andInvoiceIn(List<String> values) {
            addCriterion("invoice in", values, "invoice");
            return (Criteria) this;
        }

        public Criteria andInvoiceNotIn(List<String> values) {
            addCriterion("invoice not in", values, "invoice");
            return (Criteria) this;
        }

        public Criteria andInvoiceBetween(String value1, String value2) {
            addCriterion("invoice between", value1, value2, "invoice");
            return (Criteria) this;
        }

        public Criteria andInvoiceNotBetween(String value1, String value2) {
            addCriterion("invoice not between", value1, value2, "invoice");
            return (Criteria) this;
        }

        public Criteria andFromTypeIsNull() {
            addCriterion("from_type is null");
            return (Criteria) this;
        }

        public Criteria andFromTypeIsNotNull() {
            addCriterion("from_type is not null");
            return (Criteria) this;
        }

        public Criteria andFromTypeEqualTo(String value) {
            addCriterion("from_type =", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeNotEqualTo(String value) {
            addCriterion("from_type <>", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeGreaterThan(String value) {
            addCriterion("from_type >", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeGreaterThanOrEqualTo(String value) {
            addCriterion("from_type >=", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeLessThan(String value) {
            addCriterion("from_type <", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeLessThanOrEqualTo(String value) {
            addCriterion("from_type <=", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeLike(String value) {
            addCriterion("from_type like", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeNotLike(String value) {
            addCriterion("from_type not like", value, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeIn(List<String> values) {
            addCriterion("from_type in", values, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeNotIn(List<String> values) {
            addCriterion("from_type not in", values, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeBetween(String value1, String value2) {
            addCriterion("from_type between", value1, value2, "fromType");
            return (Criteria) this;
        }

        public Criteria andFromTypeNotBetween(String value1, String value2) {
            addCriterion("from_type not between", value1, value2, "fromType");
            return (Criteria) this;
        }

        public Criteria andDeliveryFeeIsNull() {
            addCriterion("delivery_fee is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryFeeIsNotNull() {
            addCriterion("delivery_fee is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryFeeEqualTo(Double value) {
            addCriterion("delivery_fee =", value, "deliveryFee");
            return (Criteria) this;
        }

        public Criteria andDeliveryFeeNotEqualTo(Double value) {
            addCriterion("delivery_fee <>", value, "deliveryFee");
            return (Criteria) this;
        }

        public Criteria andDeliveryFeeGreaterThan(Double value) {
            addCriterion("delivery_fee >", value, "deliveryFee");
            return (Criteria) this;
        }

        public Criteria andDeliveryFeeGreaterThanOrEqualTo(Double value) {
            addCriterion("delivery_fee >=", value, "deliveryFee");
            return (Criteria) this;
        }

        public Criteria andDeliveryFeeLessThan(Double value) {
            addCriterion("delivery_fee <", value, "deliveryFee");
            return (Criteria) this;
        }

        public Criteria andDeliveryFeeLessThanOrEqualTo(Double value) {
            addCriterion("delivery_fee <=", value, "deliveryFee");
            return (Criteria) this;
        }

        public Criteria andDeliveryFeeIn(List<Double> values) {
            addCriterion("delivery_fee in", values, "deliveryFee");
            return (Criteria) this;
        }

        public Criteria andDeliveryFeeNotIn(List<Double> values) {
            addCriterion("delivery_fee not in", values, "deliveryFee");
            return (Criteria) this;
        }

        public Criteria andDeliveryFeeBetween(Double value1, Double value2) {
            addCriterion("delivery_fee between", value1, value2, "deliveryFee");
            return (Criteria) this;
        }

        public Criteria andDeliveryFeeNotBetween(Double value1, Double value2) {
            addCriterion("delivery_fee not between", value1, value2, "deliveryFee");
            return (Criteria) this;
        }

        public Criteria andCostLunchBoxIsNull() {
            addCriterion("cost_lunch_box is null");
            return (Criteria) this;
        }

        public Criteria andCostLunchBoxIsNotNull() {
            addCriterion("cost_lunch_box is not null");
            return (Criteria) this;
        }

        public Criteria andCostLunchBoxEqualTo(Double value) {
            addCriterion("cost_lunch_box =", value, "costLunchBox");
            return (Criteria) this;
        }

        public Criteria andCostLunchBoxNotEqualTo(Double value) {
            addCriterion("cost_lunch_box <>", value, "costLunchBox");
            return (Criteria) this;
        }

        public Criteria andCostLunchBoxGreaterThan(Double value) {
            addCriterion("cost_lunch_box >", value, "costLunchBox");
            return (Criteria) this;
        }

        public Criteria andCostLunchBoxGreaterThanOrEqualTo(Double value) {
            addCriterion("cost_lunch_box >=", value, "costLunchBox");
            return (Criteria) this;
        }

        public Criteria andCostLunchBoxLessThan(Double value) {
            addCriterion("cost_lunch_box <", value, "costLunchBox");
            return (Criteria) this;
        }

        public Criteria andCostLunchBoxLessThanOrEqualTo(Double value) {
            addCriterion("cost_lunch_box <=", value, "costLunchBox");
            return (Criteria) this;
        }

        public Criteria andCostLunchBoxIn(List<Double> values) {
            addCriterion("cost_lunch_box in", values, "costLunchBox");
            return (Criteria) this;
        }

        public Criteria andCostLunchBoxNotIn(List<Double> values) {
            addCriterion("cost_lunch_box not in", values, "costLunchBox");
            return (Criteria) this;
        }

        public Criteria andCostLunchBoxBetween(Double value1, Double value2) {
            addCriterion("cost_lunch_box between", value1, value2, "costLunchBox");
            return (Criteria) this;
        }

        public Criteria andCostLunchBoxNotBetween(Double value1, Double value2) {
            addCriterion("cost_lunch_box not between", value1, value2, "costLunchBox");
            return (Criteria) this;
        }

        public Criteria andMemberDiscountMoneyIsNull() {
            addCriterion("member_discount_money is null");
            return (Criteria) this;
        }

        public Criteria andMemberDiscountMoneyIsNotNull() {
            addCriterion("member_discount_money is not null");
            return (Criteria) this;
        }

        public Criteria andMemberDiscountMoneyEqualTo(Double value) {
            addCriterion("member_discount_money =", value, "memberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andMemberDiscountMoneyNotEqualTo(Double value) {
            addCriterion("member_discount_money <>", value, "memberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andMemberDiscountMoneyGreaterThan(Double value) {
            addCriterion("member_discount_money >", value, "memberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andMemberDiscountMoneyGreaterThanOrEqualTo(Double value) {
            addCriterion("member_discount_money >=", value, "memberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andMemberDiscountMoneyLessThan(Double value) {
            addCriterion("member_discount_money <", value, "memberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andMemberDiscountMoneyLessThanOrEqualTo(Double value) {
            addCriterion("member_discount_money <=", value, "memberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andMemberDiscountMoneyIn(List<Double> values) {
            addCriterion("member_discount_money in", values, "memberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andMemberDiscountMoneyNotIn(List<Double> values) {
            addCriterion("member_discount_money not in", values, "memberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andMemberDiscountMoneyBetween(Double value1, Double value2) {
            addCriterion("member_discount_money between", value1, value2, "memberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andMemberDiscountMoneyNotBetween(Double value1, Double value2) {
            addCriterion("member_discount_money not between", value1, value2, "memberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andMerchantMemberDiscountMoneyIsNull() {
            addCriterion("merchant_member_discount_money is null");
            return (Criteria) this;
        }

        public Criteria andMerchantMemberDiscountMoneyIsNotNull() {
            addCriterion("merchant_member_discount_money is not null");
            return (Criteria) this;
        }

        public Criteria andMerchantMemberDiscountMoneyEqualTo(Double value) {
            addCriterion("merchant_member_discount_money =", value, "merchantMemberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andMerchantMemberDiscountMoneyNotEqualTo(Double value) {
            addCriterion("merchant_member_discount_money <>", value, "merchantMemberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andMerchantMemberDiscountMoneyGreaterThan(Double value) {
            addCriterion("merchant_member_discount_money >", value, "merchantMemberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andMerchantMemberDiscountMoneyGreaterThanOrEqualTo(Double value) {
            addCriterion("merchant_member_discount_money >=", value, "merchantMemberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andMerchantMemberDiscountMoneyLessThan(Double value) {
            addCriterion("merchant_member_discount_money <", value, "merchantMemberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andMerchantMemberDiscountMoneyLessThanOrEqualTo(Double value) {
            addCriterion("merchant_member_discount_money <=", value, "merchantMemberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andMerchantMemberDiscountMoneyIn(List<Double> values) {
            addCriterion("merchant_member_discount_money in", values, "merchantMemberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andMerchantMemberDiscountMoneyNotIn(List<Double> values) {
            addCriterion("merchant_member_discount_money not in", values, "merchantMemberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andMerchantMemberDiscountMoneyBetween(Double value1, Double value2) {
            addCriterion("merchant_member_discount_money between", value1, value2, "merchantMemberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andMerchantMemberDiscountMoneyNotBetween(Double value1, Double value2) {
            addCriterion("merchant_member_discount_money not between", value1, value2, "merchantMemberDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andDineInDiscountMoneyIsNull() {
            addCriterion("dine_in_discount_money is null");
            return (Criteria) this;
        }

        public Criteria andDineInDiscountMoneyIsNotNull() {
            addCriterion("dine_in_discount_money is not null");
            return (Criteria) this;
        }

        public Criteria andDineInDiscountMoneyEqualTo(Double value) {
            addCriterion("dine_in_discount_money =", value, "dineInDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andDineInDiscountMoneyNotEqualTo(Double value) {
            addCriterion("dine_in_discount_money <>", value, "dineInDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andDineInDiscountMoneyGreaterThan(Double value) {
            addCriterion("dine_in_discount_money >", value, "dineInDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andDineInDiscountMoneyGreaterThanOrEqualTo(Double value) {
            addCriterion("dine_in_discount_money >=", value, "dineInDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andDineInDiscountMoneyLessThan(Double value) {
            addCriterion("dine_in_discount_money <", value, "dineInDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andDineInDiscountMoneyLessThanOrEqualTo(Double value) {
            addCriterion("dine_in_discount_money <=", value, "dineInDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andDineInDiscountMoneyIn(List<Double> values) {
            addCriterion("dine_in_discount_money in", values, "dineInDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andDineInDiscountMoneyNotIn(List<Double> values) {
            addCriterion("dine_in_discount_money not in", values, "dineInDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andDineInDiscountMoneyBetween(Double value1, Double value2) {
            addCriterion("dine_in_discount_money between", value1, value2, "dineInDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andDineInDiscountMoneyNotBetween(Double value1, Double value2) {
            addCriterion("dine_in_discount_money not between", value1, value2, "dineInDiscountMoney");
            return (Criteria) this;
        }

        public Criteria andRechargeSrcIsNull() {
            addCriterion("recharge_src is null");
            return (Criteria) this;
        }

        public Criteria andRechargeSrcIsNotNull() {
            addCriterion("recharge_src is not null");
            return (Criteria) this;
        }

        public Criteria andRechargeSrcEqualTo(Integer value) {
            addCriterion("recharge_src =", value, "rechargeSrc");
            return (Criteria) this;
        }

        public Criteria andRechargeSrcNotEqualTo(Integer value) {
            addCriterion("recharge_src <>", value, "rechargeSrc");
            return (Criteria) this;
        }

        public Criteria andRechargeSrcGreaterThan(Integer value) {
            addCriterion("recharge_src >", value, "rechargeSrc");
            return (Criteria) this;
        }

        public Criteria andRechargeSrcGreaterThanOrEqualTo(Integer value) {
            addCriterion("recharge_src >=", value, "rechargeSrc");
            return (Criteria) this;
        }

        public Criteria andRechargeSrcLessThan(Integer value) {
            addCriterion("recharge_src <", value, "rechargeSrc");
            return (Criteria) this;
        }

        public Criteria andRechargeSrcLessThanOrEqualTo(Integer value) {
            addCriterion("recharge_src <=", value, "rechargeSrc");
            return (Criteria) this;
        }

        public Criteria andRechargeSrcIn(List<Integer> values) {
            addCriterion("recharge_src in", values, "rechargeSrc");
            return (Criteria) this;
        }

        public Criteria andRechargeSrcNotIn(List<Integer> values) {
            addCriterion("recharge_src not in", values, "rechargeSrc");
            return (Criteria) this;
        }

        public Criteria andRechargeSrcBetween(Integer value1, Integer value2) {
            addCriterion("recharge_src between", value1, value2, "rechargeSrc");
            return (Criteria) this;
        }

        public Criteria andRechargeSrcNotBetween(Integer value1, Integer value2) {
            addCriterion("recharge_src not between", value1, value2, "rechargeSrc");
            return (Criteria) this;
        }

        public Criteria andInviteIdIsNull() {
            addCriterion("invite_id is null");
            return (Criteria) this;
        }

        public Criteria andInviteIdIsNotNull() {
            addCriterion("invite_id is not null");
            return (Criteria) this;
        }

        public Criteria andInviteIdEqualTo(Long value) {
            addCriterion("invite_id =", value, "inviteId");
            return (Criteria) this;
        }

        public Criteria andInviteIdNotEqualTo(Long value) {
            addCriterion("invite_id <>", value, "inviteId");
            return (Criteria) this;
        }

        public Criteria andInviteIdGreaterThan(Long value) {
            addCriterion("invite_id >", value, "inviteId");
            return (Criteria) this;
        }

        public Criteria andInviteIdGreaterThanOrEqualTo(Long value) {
            addCriterion("invite_id >=", value, "inviteId");
            return (Criteria) this;
        }

        public Criteria andInviteIdLessThan(Long value) {
            addCriterion("invite_id <", value, "inviteId");
            return (Criteria) this;
        }

        public Criteria andInviteIdLessThanOrEqualTo(Long value) {
            addCriterion("invite_id <=", value, "inviteId");
            return (Criteria) this;
        }

        public Criteria andInviteIdIn(List<Long> values) {
            addCriterion("invite_id in", values, "inviteId");
            return (Criteria) this;
        }

        public Criteria andInviteIdNotIn(List<Long> values) {
            addCriterion("invite_id not in", values, "inviteId");
            return (Criteria) this;
        }

        public Criteria andInviteIdBetween(Long value1, Long value2) {
            addCriterion("invite_id between", value1, value2, "inviteId");
            return (Criteria) this;
        }

        public Criteria andInviteIdNotBetween(Long value1, Long value2) {
            addCriterion("invite_id not between", value1, value2, "inviteId");
            return (Criteria) this;
        }

        public Criteria andAgentIdIsNull() {
            addCriterion("agent_id is null");
            return (Criteria) this;
        }

        public Criteria andAgentIdIsNotNull() {
            addCriterion("agent_id is not null");
            return (Criteria) this;
        }

        public Criteria andAgentIdEqualTo(Integer value) {
            addCriterion("agent_id =", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdNotEqualTo(Integer value) {
            addCriterion("agent_id <>", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdGreaterThan(Integer value) {
            addCriterion("agent_id >", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("agent_id >=", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdLessThan(Integer value) {
            addCriterion("agent_id <", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdLessThanOrEqualTo(Integer value) {
            addCriterion("agent_id <=", value, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdIn(List<Integer> values) {
            addCriterion("agent_id in", values, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdNotIn(List<Integer> values) {
            addCriterion("agent_id not in", values, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdBetween(Integer value1, Integer value2) {
            addCriterion("agent_id between", value1, value2, "agentId");
            return (Criteria) this;
        }

        public Criteria andAgentIdNotBetween(Integer value1, Integer value2) {
            addCriterion("agent_id not between", value1, value2, "agentId");
            return (Criteria) this;
        }

        public Criteria andIsMerchantDeliveryIsNull() {
            addCriterion("is_merchant_delivery is null");
            return (Criteria) this;
        }

        public Criteria andIsMerchantDeliveryIsNotNull() {
            addCriterion("is_merchant_delivery is not null");
            return (Criteria) this;
        }

        public Criteria andIsMerchantDeliveryEqualTo(String value) {
            addCriterion("is_merchant_delivery =", value, "isMerchantDelivery");
            return (Criteria) this;
        }

        public Criteria andIsMerchantDeliveryNotEqualTo(String value) {
            addCriterion("is_merchant_delivery <>", value, "isMerchantDelivery");
            return (Criteria) this;
        }

        public Criteria andIsMerchantDeliveryGreaterThan(String value) {
            addCriterion("is_merchant_delivery >", value, "isMerchantDelivery");
            return (Criteria) this;
        }

        public Criteria andIsMerchantDeliveryGreaterThanOrEqualTo(String value) {
            addCriterion("is_merchant_delivery >=", value, "isMerchantDelivery");
            return (Criteria) this;
        }

        public Criteria andIsMerchantDeliveryLessThan(String value) {
            addCriterion("is_merchant_delivery <", value, "isMerchantDelivery");
            return (Criteria) this;
        }

        public Criteria andIsMerchantDeliveryLessThanOrEqualTo(String value) {
            addCriterion("is_merchant_delivery <=", value, "isMerchantDelivery");
            return (Criteria) this;
        }

        public Criteria andIsMerchantDeliveryLike(String value) {
            addCriterion("is_merchant_delivery like", value, "isMerchantDelivery");
            return (Criteria) this;
        }

        public Criteria andIsMerchantDeliveryNotLike(String value) {
            addCriterion("is_merchant_delivery not like", value, "isMerchantDelivery");
            return (Criteria) this;
        }

        public Criteria andIsMerchantDeliveryIn(List<String> values) {
            addCriterion("is_merchant_delivery in", values, "isMerchantDelivery");
            return (Criteria) this;
        }

        public Criteria andIsMerchantDeliveryNotIn(List<String> values) {
            addCriterion("is_merchant_delivery not in", values, "isMerchantDelivery");
            return (Criteria) this;
        }

        public Criteria andIsMerchantDeliveryBetween(String value1, String value2) {
            addCriterion("is_merchant_delivery between", value1, value2, "isMerchantDelivery");
            return (Criteria) this;
        }

        public Criteria andIsMerchantDeliveryNotBetween(String value1, String value2) {
            addCriterion("is_merchant_delivery not between", value1, value2, "isMerchantDelivery");
            return (Criteria) this;
        }

        public Criteria andLwkIdIsNull() {
            addCriterion("lwk_id is null");
            return (Criteria) this;
        }

        public Criteria andLwkIdIsNotNull() {
            addCriterion("lwk_id is not null");
            return (Criteria) this;
        }

        public Criteria andLwkIdEqualTo(String value) {
            addCriterion("lwk_id =", value, "lwkId");
            return (Criteria) this;
        }

        public Criteria andLwkIdNotEqualTo(String value) {
            addCriterion("lwk_id <>", value, "lwkId");
            return (Criteria) this;
        }

        public Criteria andLwkIdGreaterThan(String value) {
            addCriterion("lwk_id >", value, "lwkId");
            return (Criteria) this;
        }

        public Criteria andLwkIdGreaterThanOrEqualTo(String value) {
            addCriterion("lwk_id >=", value, "lwkId");
            return (Criteria) this;
        }

        public Criteria andLwkIdLessThan(String value) {
            addCriterion("lwk_id <", value, "lwkId");
            return (Criteria) this;
        }

        public Criteria andLwkIdLessThanOrEqualTo(String value) {
            addCriterion("lwk_id <=", value, "lwkId");
            return (Criteria) this;
        }

        public Criteria andLwkIdLike(String value) {
            addCriterion("lwk_id like", value, "lwkId");
            return (Criteria) this;
        }

        public Criteria andLwkIdNotLike(String value) {
            addCriterion("lwk_id not like", value, "lwkId");
            return (Criteria) this;
        }

        public Criteria andLwkIdIn(List<String> values) {
            addCriterion("lwk_id in", values, "lwkId");
            return (Criteria) this;
        }

        public Criteria andLwkIdNotIn(List<String> values) {
            addCriterion("lwk_id not in", values, "lwkId");
            return (Criteria) this;
        }

        public Criteria andLwkIdBetween(String value1, String value2) {
            addCriterion("lwk_id between", value1, value2, "lwkId");
            return (Criteria) this;
        }

        public Criteria andLwkIdNotBetween(String value1, String value2) {
            addCriterion("lwk_id not between", value1, value2, "lwkId");
            return (Criteria) this;
        }

        public Criteria andLwkMarkIsNull() {
            addCriterion("lwk_mark is null");
            return (Criteria) this;
        }

        public Criteria andLwkMarkIsNotNull() {
            addCriterion("lwk_mark is not null");
            return (Criteria) this;
        }

        public Criteria andLwkMarkEqualTo(String value) {
            addCriterion("lwk_mark =", value, "lwkMark");
            return (Criteria) this;
        }

        public Criteria andLwkMarkNotEqualTo(String value) {
            addCriterion("lwk_mark <>", value, "lwkMark");
            return (Criteria) this;
        }

        public Criteria andLwkMarkGreaterThan(String value) {
            addCriterion("lwk_mark >", value, "lwkMark");
            return (Criteria) this;
        }

        public Criteria andLwkMarkGreaterThanOrEqualTo(String value) {
            addCriterion("lwk_mark >=", value, "lwkMark");
            return (Criteria) this;
        }

        public Criteria andLwkMarkLessThan(String value) {
            addCriterion("lwk_mark <", value, "lwkMark");
            return (Criteria) this;
        }

        public Criteria andLwkMarkLessThanOrEqualTo(String value) {
            addCriterion("lwk_mark <=", value, "lwkMark");
            return (Criteria) this;
        }

        public Criteria andLwkMarkLike(String value) {
            addCriterion("lwk_mark like", value, "lwkMark");
            return (Criteria) this;
        }

        public Criteria andLwkMarkNotLike(String value) {
            addCriterion("lwk_mark not like", value, "lwkMark");
            return (Criteria) this;
        }

        public Criteria andLwkMarkIn(List<String> values) {
            addCriterion("lwk_mark in", values, "lwkMark");
            return (Criteria) this;
        }

        public Criteria andLwkMarkNotIn(List<String> values) {
            addCriterion("lwk_mark not in", values, "lwkMark");
            return (Criteria) this;
        }

        public Criteria andLwkMarkBetween(String value1, String value2) {
            addCriterion("lwk_mark between", value1, value2, "lwkMark");
            return (Criteria) this;
        }

        public Criteria andLwkMarkNotBetween(String value1, String value2) {
            addCriterion("lwk_mark not between", value1, value2, "lwkMark");
            return (Criteria) this;
        }

        public Criteria andLwkFlagIsNull() {
            addCriterion("lwk_flag is null");
            return (Criteria) this;
        }

        public Criteria andLwkFlagIsNotNull() {
            addCriterion("lwk_flag is not null");
            return (Criteria) this;
        }

        public Criteria andLwkFlagEqualTo(String value) {
            addCriterion("lwk_flag =", value, "lwkFlag");
            return (Criteria) this;
        }

        public Criteria andLwkFlagNotEqualTo(String value) {
            addCriterion("lwk_flag <>", value, "lwkFlag");
            return (Criteria) this;
        }

        public Criteria andLwkFlagGreaterThan(String value) {
            addCriterion("lwk_flag >", value, "lwkFlag");
            return (Criteria) this;
        }

        public Criteria andLwkFlagGreaterThanOrEqualTo(String value) {
            addCriterion("lwk_flag >=", value, "lwkFlag");
            return (Criteria) this;
        }

        public Criteria andLwkFlagLessThan(String value) {
            addCriterion("lwk_flag <", value, "lwkFlag");
            return (Criteria) this;
        }

        public Criteria andLwkFlagLessThanOrEqualTo(String value) {
            addCriterion("lwk_flag <=", value, "lwkFlag");
            return (Criteria) this;
        }

        public Criteria andLwkFlagLike(String value) {
            addCriterion("lwk_flag like", value, "lwkFlag");
            return (Criteria) this;
        }

        public Criteria andLwkFlagNotLike(String value) {
            addCriterion("lwk_flag not like", value, "lwkFlag");
            return (Criteria) this;
        }

        public Criteria andLwkFlagIn(List<String> values) {
            addCriterion("lwk_flag in", values, "lwkFlag");
            return (Criteria) this;
        }

        public Criteria andLwkFlagNotIn(List<String> values) {
            addCriterion("lwk_flag not in", values, "lwkFlag");
            return (Criteria) this;
        }

        public Criteria andLwkFlagBetween(String value1, String value2) {
            addCriterion("lwk_flag between", value1, value2, "lwkFlag");
            return (Criteria) this;
        }

        public Criteria andLwkFlagNotBetween(String value1, String value2) {
            addCriterion("lwk_flag not between", value1, value2, "lwkFlag");
            return (Criteria) this;
        }

        public Criteria andFlashOrderIdIsNull() {
            addCriterion("flash_order_id is null");
            return (Criteria) this;
        }

        public Criteria andFlashOrderIdIsNotNull() {
            addCriterion("flash_order_id is not null");
            return (Criteria) this;
        }

        public Criteria andFlashOrderIdEqualTo(Long value) {
            addCriterion("flash_order_id =", value, "flashOrderId");
            return (Criteria) this;
        }

        public Criteria andFlashOrderIdNotEqualTo(Long value) {
            addCriterion("flash_order_id <>", value, "flashOrderId");
            return (Criteria) this;
        }

        public Criteria andFlashOrderIdGreaterThan(Long value) {
            addCriterion("flash_order_id >", value, "flashOrderId");
            return (Criteria) this;
        }

        public Criteria andFlashOrderIdGreaterThanOrEqualTo(Long value) {
            addCriterion("flash_order_id >=", value, "flashOrderId");
            return (Criteria) this;
        }

        public Criteria andFlashOrderIdLessThan(Long value) {
            addCriterion("flash_order_id <", value, "flashOrderId");
            return (Criteria) this;
        }

        public Criteria andFlashOrderIdLessThanOrEqualTo(Long value) {
            addCriterion("flash_order_id <=", value, "flashOrderId");
            return (Criteria) this;
        }

        public Criteria andFlashOrderIdIn(List<Long> values) {
            addCriterion("flash_order_id in", values, "flashOrderId");
            return (Criteria) this;
        }

        public Criteria andFlashOrderIdNotIn(List<Long> values) {
            addCriterion("flash_order_id not in", values, "flashOrderId");
            return (Criteria) this;
        }

        public Criteria andFlashOrderIdBetween(Long value1, Long value2) {
            addCriterion("flash_order_id between", value1, value2, "flashOrderId");
            return (Criteria) this;
        }

        public Criteria andFlashOrderIdNotBetween(Long value1, Long value2) {
            addCriterion("flash_order_id not between", value1, value2, "flashOrderId");
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