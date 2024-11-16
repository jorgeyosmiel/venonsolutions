import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '1m', target: 100 },
        { duration: '10m', target: 1000 },
        { duration: '5m', target: 1000 },
        { duration: '1m', target: 0 },
    ],
    ext: {
        dashboard: {
            output: "dashboard.html", // Single dashboard for all tests
        },
    },
};

const BASE_URL = 'http://localhost:8080/api/v3/pet/';
const petIds = [10, 444, 999];
const statuses = ['available', 'pending', 'sold'];
const tags = ['tag1', 'tag2'];

// Test getPet endpoint
function testGetPet() {
    const petId = petIds[__VU % petIds.length];
    const petUrl = `${BASE_URL}/${petId}`;
    const petResponse = http.get(petUrl);

    check(petResponse, {
        'getPet - status is 200': (r) => r.status === 200,
        'getPet - response contains pet details': (r) => {
            const body = JSON.parse(r.body);
            return body && body.id === petId;
        },
    });

    sleep(1);
}

// Test getByStatus endpoint
function testGetByStatus() {
    const status = statuses[__VU % statuses.length];
    const statusUrl = `${BASE_URL}/findByStatus?status=${status}`;
    const statusResponse = http.get(statusUrl);

    check(statusResponse, {
        'getByStatus - status is 200': (r) => r.status === 200,
        'getByStatus - response contains pets': (r) => {
            const body = JSON.parse(r.body);
            return Array.isArray(body) && body.length > 0;
        },
    });

    sleep(1);
}

// Test getByTags endpoint
function testGetByTags() {
    const tagsUrl = `${BASE_URL}/findByTags?tags=${tags.join(',')}`;
    const tagsResponse = http.get(tagsUrl);

    check(tagsResponse, {
        'getByTags - status is 200': (r) => r.status === 200,
        'getByTags - response contains pets': (r) => {
            const body = JSON.parse(r.body);
            return Array.isArray(body) && body.length > 0;
        },
    });

    sleep(1);
}

export default function () {
    testGetPet();
    testGetByStatus();
    testGetByTags();
}
