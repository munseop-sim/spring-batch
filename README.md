# 날씨데이터를 수집 spring Batch 

## 기능
1. data.go.kr에서 제공하는 날씨데이터 수집 API
2. 멀티서버 환경을 고려하여 동시성 이슈 처리 (redis이용)
3. spring batch 학습

## spring batch
1. ItemReader
2. Process
3. ItemWriter
4. Job
5. Schedule
6. TestCode
   
## 데이터베이스 
1. H2 (in-memory) 
- 별도 설정 필요없음
2. mysql (docker)
- https://github.com/munseop-sim/spring-batch/wiki/docker#redis
3. redis (docker)
- https://github.com/munseop-sim/spring-batch/wiki/docker#mysql

