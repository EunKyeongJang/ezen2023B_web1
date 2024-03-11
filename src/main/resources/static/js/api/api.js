console.log("api.js");

var map = new kakao.maps.Map(document.getElementById('map'), { // 지도를 표시할 div
        center : new kakao.maps.LatLng(36.2683, 127.6358), // 지도의 중심좌표
        level : 14 // 지도의 확대 레벨
    });

    // 마커 클러스터러(마커가 여러개일때 합쳐지는 효과)를 생성합니다
    var clusterer = new kakao.maps.MarkerClusterer({
        map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
        averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
        minLevel: 10 // 클러스터 할 최소 지도 레벨
    });

    //3. !!
    // 데이터를 가져오기 위해 jQuery를 사용합니다
    // 데이터를 가져와 마커를 생성하고 클러스터러 객체에 넘겨줍니다
    /*
        $.ajax({ url : , method : , data : {} , success : (r)=>{} })
        >>간소화
        $.HTTP메소드(url, (r) => {})
        $.get(url, (r) => {})
        $.post(url, (r) => {})
        $.put(url, (r) => {})
        $.delete(url, (r) => {})
    */
    //데이터 호출해서 map이용한 마커 1개 생성 후 markers 담는다. map 반복문 종료 후 markers를 클러스터에 담아준다.
    $.ajax({
        url : "https://api.odcloud.kr/api/15109590/v1/uddi:3e550608-d205-411b-a92d-e7fd2278b7bc?page=1&perPage=100&serviceKey=UdKU55Ou1k%2FbVPGoFGuDIG9ZguL1BE6hUMf%2B%2FbyvLHhF8yuunoXj7ikFnjQwE3CbxlWHG7ODVN0NmEla59Vrcw%3D%3D",
        method : "Get",
        success : (response) => {
            var markers = r.data.map((object) => {
                //마커 1개 만들어서 리턴 해서 markers에 대입
                return new kakao.maps.Marker({
                    //마커의 위치
                    position : new kakao.maps.LatLng(object.식당위도, object.식당경도)
                });//return map end
            });//map end
            // 클러스터러에 마커들을 추가합니다
            clusterer.addMarkers(markers);
        }//success end
    })//ajax end

    //3-2. 방식 2
    /*$.get("https://api.odcloud.kr/api/15109590/v1/uddi:3e550608-d205-411b-a92d-e7fd2278b7bc?page=1&perPage=100&serviceKey=UdKU55Ou1k%2FbVPGoFGuDIG9ZguL1BE6hUMf%2B%2FbyvLHhF8yuunoXj7ikFnjQwE3CbxlWHG7ODVN0NmEla59Vrcw%3D%3D",
        function(r) {
        // 데이터에서 좌표 값을 가지고 마커를 표시합니다
        // 마커 클러스터러로 관리할 마커 객체는 생성할 때 지도 객체를 설정하지 않습니다
        var markers = r.data.map((object) => {
            return new kakao.maps.Marker({
                position : new kakao.maps.LatLng(object.식당위도, object.식당경도)
            });
        });

        // 클러스터러에 마커들을 추가합니다
        clusterer.addMarkers(markers);
    });*/

//안산시 강우량 api
$.ajax({
    url : "https://api.odcloud.kr/api/15111852/v1/uddi:71ee8321-fea5-4818-ade4-9425e0439096?page=1&perPage=10&serviceKey=UdKU55Ou1k%2FbVPGoFGuDIG9ZguL1BE6hUMf%2B%2FbyvLHhF8yuunoXj7ikFnjQwE3CbxlWHG7ODVN0NmEla59Vrcw%3D%3D",
    method : "Get",
    success : (r)=>{
        console.log(r);
        let apiTable1=document.querySelector('.apiTable1');
        let html=``;

        r.data.forEach((object)=>{
            html+=`<tr>
                       <td> ${object.관리기관명} </td>
                       <td> ${object.날짜} </td>
                       <td> ${object.시도명} ${object.시군구명} ${object.읍면동} </td>
                       <td> ${object['우량(mm)']} </td>
                   </tr>`;
        })
        apiTable1.innerHTML=html;
    }//success end
})//ajax end

//안산시 원곡동 일반음식점
$.ajax({
    url : "https://api.odcloud.kr/api/15109590/v1/uddi:3e550608-d205-411b-a92d-e7fd2278b7bc?page=1&perPage=100&serviceKey=UdKU55Ou1k%2FbVPGoFGuDIG9ZguL1BE6hUMf%2B%2FbyvLHhF8yuunoXj7ikFnjQwE3CbxlWHG7ODVN0NmEla59Vrcw%3D%3D",
    method : "Get",
    success : (result) => {
        console.log(result);
        let apiTable2=document.querySelector('.apiTable2');
            let html=``;

            result.data.forEach((object)=>{
                html+=`<tr>
                           <td> ${object.사업장명} </td>
                           <td> ${object.도로명전체주소} </td>
                           <td> ${object.대표메뉴1}</td>
                           <td> ${object.메뉴가격1.toLocaleString()}</td>
                           <td> ${object.대표전화}</td>
                           <td> ${object['주차 가능']}</td>
                       </tr>`;
            })
            apiTable2.innerHTML=html;
    }//success end
})//ajax end


/*
    객체명.속성명===객체명['속성명']
*/
