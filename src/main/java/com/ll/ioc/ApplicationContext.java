package com.ll.ioc;

import com.ll.domain.testPost.testPost.repository.TestPostRepository;
import com.ll.domain.testPost.testPost.service.TestPostService;

import java.util.HashMap;
import java.util.Map;

// 스프링의 IoC 컨테이너(ApplicationContext)를 흉내 낸 클래스
// - 객체(빈)를 "내가 new 하지 않고" 컨테이너에게 이름으로 달라고 한다 → 제어의 역전(IoC)
// - 한 번 만든 빈은 저장해 두고 계속 같은 객체를 준다 → 싱글톤
public class ApplicationContext {
    // 빈 이름 → 만들어 둔 빈 객체
    private final Map<String, Object> beans = new HashMap<>();

    public ApplicationContext() {

    }

    @SuppressWarnings("unchecked")
    public <T> T genBean(String beanName) {
        // 1. 이미 만든 빈이 있으면 그대로 돌려준다 (싱글톤)
        Object bean = beans.get(beanName);

        if (bean != null) return (T) bean;

        // 2. 없으면 처음 한 번만 만들어서 저장한다
        bean = createBean(beanName);
        beans.put(beanName, bean);

        return (T) bean;
    }

    // v1: 어떤 빈을 어떻게 만드는지 하드코딩한다
    // 다른 빈이 필요하면 new 하지 않고 genBean 으로 컨테이너에서 받아 생성자에 넣는다 → 의존성 주입(DI)
    private Object createBean(String beanName) {
        return switch (beanName) {
            case "testPostRepository" -> new TestPostRepository();
            case "testPostService" -> new TestPostService(genBean("testPostRepository"));
            default -> throw new IllegalArgumentException("등록되지 않은 빈입니다: " + beanName);
        };
    }
}
