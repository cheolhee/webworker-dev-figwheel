# webworker-dev-figwheel

figwheel 을 이용한 Web Worker 개발 예제

## Overview ##

WebWorker는 DuplicatedWorkerGlobalScope 라는 별도의 Global Context에서 동작하기 때문에
1. DOM을 사용할 수 없다.
2. 별도의 .js를 만들어야 한다.

이런 이유로 figwheel REPL이 제대로 동작하지 않습니다.

해결 방법
1. 컴파일 옵션에 :target :webworker 추가
2. 소스 폴더 분리, build-id 분리.


## figwheel 실행

     lein figwheel dev dev-worker


화면이 뜨면 소인수분해를 계산하는 간단한 웹 화면이 뜹니다. 

웹 컨텍스트에서 바로 계산할 수도 있고
WebWorker 컨텍스트에서 계산할 수도 있습니다. 

하단의 카운터는 0.1초마다 숫자가 올라갑니다. setTimeout이 제대로 동작하는지 확인하는 용도입니다.
그 밑의 Click Me! 버튼은 UI입력이 동작하는지 확인하기 위한 용도입니다.

## 실험

### 983476 계산

이건 웹 컨텍스트, WebWorker 컨텍스트 둘 다 결과가 바로 나옵니다.

### 2543224921 계산

"Find Prime Factors *Here*" 버튼을 클릭하면 하단의 카운터가 멈추고 ClickMe! 버튼도 입력이 안됩니다.
30초 정도 웹 화면이 정지됩니다.

### 2543224921 WebWork로 계산

"Find Prime Factors *on WebWorker*" 버튼을 클릭하면 하단의 카운터는 여전히 잘 동작하고 ClickMe! 버튼도 입력이 잘 됩니다.
웹 화면이 정지되지 않습니다. 


## project.clj 구성

source 경로를 분리합니다.

         :source-paths ["src" "src_worker"]


빌드 id 를 분리합니다.

         {:id "dev"
          :source-paths ["src"]
          :figwheel {:on-jsload "webworker-dev-figwheel.core/on-js-reload"
                     :open-urls ["http://localhost:4449/index.html"]}
           .....
         {:id "dev-worker"
          :source-paths ["src_worker"]
          ......
          

:target :webworker 를 추가합니다.          
그리고 반드시 주의해야 할 점.  :asset-path 를 반드시 *절대 경로*로 잡아야 합니다.        


         {:id "dev-worker"
          :source-paths ["src_worker"]
          :figwheel {:on-jsload "webworker-dev-figwheel.worker/on-js-reload"}
          :compiler {:main webworker-dev-figwheel.worker
                     :asset-path "/js/compiled/out_worker" ; <----- HERE !!!
                     :target :webworker
           ............


왜냐하면 index.html은 루트에 있기 때문에 js/... 로 시작해도 이미 루트경로이지만
WebWorker는 

        worker = new WebWorker("js/compiled/worker.js")

처럼 이미 /js/compiled/ 경로 상에서 살행되기 때문입니다. 


다른 부분은 일반적인 project.clj와 큰 차이는 없습니다.


## License

Copyright © 2018 cheolhee

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
