import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    vus: 50,             // 동시 사용자 수
    duration: '30s',     // 테스트 지속 시간
};

export default function () {
    const res = http.get('http://127.0.0.1:8080/api/v1/products/stress?brandId=55&sortBy=likes_desc');

    check(res, {
        'status is 200': (r) => r.status === 200,
        'response is not empty': (r) => r.body && r.body.length > 0,
    });

    sleep(1); // 1초 대기 (사용자 think time 시뮬레이션)
}
