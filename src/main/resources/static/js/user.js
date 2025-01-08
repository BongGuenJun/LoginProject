$(function () {

    /////////////////////1번추가항목////////////////////////////////////////
    // 로그인 폼 유효성 검사
    $("#loginForm").submit(function() {

        let id = $("#id").val();
        let pass1 = $("#pass1").val();
        let pass2 = $("pass2").val();

        if(id.length <= 0) {
            alert("아이디를 입력해주세요");
            $("#userId").focus();
            return false;
        }
        if(pass1.length <= 0) {
            alert("비밀번호를 입력해주세요");
            $("#userPass").focus();
            return false;
        }
        if(pass2.length <= 0) {
            alert("비밀번호 확인란을 입력해주세요");
            $("#userPass").focus();
            return false;
        }
    });
    //////////////////////////////////////////////////////////////////////

    // 영문 + 숫자만 받기//////////////////////////////////////////////////
    $("#id").on("keyup", function() {

        let regExp = /[^a-zA-Z0-9]/gi;
        if(regExp.test($("#id").val())) {
            alert("영문자와 숫자만 입력할 수 있습니다.");
            $(this).val($(this).val().replace(regExp, ""))
        }
    });

    $("#pass1").on("keyup", inputCharReplace);
    $("#pass2").on("keyup", inputCharReplace);
    $("#emailId").on("keyup", inputCharReplace);
    $("#emailDomain").on("keyup", inputEmailDomainReplace);

    /////////////////////////////////////////////////////////////////////////////

    ///////////// 아이디 중복확인 용 새창을 띄워주는 함수/////////////////////
    $("#btnOverlapId").on("click", function () {
        let id = $("#id").val(); // 입력된 아이디 값 가져오기

        if (id.length === 0) { // 아이디 입력 여부 확인
            alert("아이디를 입력해주세요");
            return false;
        }
        if (id.length < 5) { // 아이디 길이 확인
            alert("아이디는 5자 이상이어야 합니다.");
            return false;
        }

        // URL 생성 및 로그 출력
        const url = "/overlapIdCheck?id=" + encodeURIComponent(id); // 입력값 URL 인코딩
        console.log("Requesting URL:", url); // 디버깅용 로그 출력

        // 새 창 열기
        window.open(url, "idCheck", "toolbar=no, scrollbars=no, resizable=no, status=no, menubar=no, width=500, height=330");
    });
    //////////////////////////////////////////////////////////////

    ////member update 부분 현제는 사용한하는 부분 주의///////////////////////////////////
    $("#memberUpdateForm").on("submit", function(e) {

        // 비밀번호 확인 유무 체크
        if(! $("#btnPassCheck").attr("disabled")) {
            alert("기존 비밀번호를 확인해 주세요");
            return false;
        }

        return joinFormCheck();
    });

    $("#btnPassCheck").click(function() {
        let oldPass = $("#oldPass").val();
        let oldId = $("#id").val();

        if($.trim(oldPass).length == 0) {
            alert("기존 비밀번호를 입력해주세요");
            return false;
        }

        // 서로 보낼 데이터  id=midas&pass=1234
        let data = "id=" + oldId + "&pass=" + oldPass;

        // 비동기 통신(Ajax)
        $.ajax({
            url: "passCheck.ajax",
            type: "get",
            data: data,
            dataType: "json",
            success: function(resData) {
                console.log(resData.result);
                if(resData.result) {
                    alert("비밀번호가 확인되었습니다.");
                    $("#btnPassCheck").attr("disabled", true)
                    $("#oldPass").attr("readonly", true);
                    $("#pass1").focus();
                } else {
                    alert("기존 비밀번호가 틀립니다.");
                    $("#oldPass").val("").focus();
                }
            },
            error: function(xhr, status) {
                console.log("error : " + status);
            }
        });
    });
///////////////////////////////////////////////////////////////
//////// 회원가입 폼이 전송될 때 유효성 검사////////////////////
    $("#joinForm").submit(function() {
        return joinFormCheck();
    });
//////////////////////////////////////////////////////////

    ////////////////셀렉트 박스에서 선택된 도메인을 설정하는 함수//////////////////
    $("#selectDomain").on("change", function() {
        let str = $(this).val();
        if(str == '직접입력') {
            $("#emailDomain").val("");
            $("#emailDomain").attr("readonly", false);
            $("#emailDomain").focus();
        } else if(str == '네이버') {
            $("#emailDomain").val("naver.com");
            $("#emailDomain").attr("readonly", true);
        } else if(str == '구글') {
            $("#emailDomain").val("gmail.com");
            $("#emailDomain").attr("readonly", true);
        }
    });
////////////////////////////////////////////////////////////

//////////우편번호 검색 시 api사용함수///////////////////
    $("#btnZipcode").click(findZipcode);
///////////////////////////////

//////////////////아이디 사용버튼 클릭시 창을 받고 부모창 폼에 입력해주는 함수////////////
    $("#btnIdCheckClose").on("click", function() {
        let id = $(this).attr("data-id-value");
        opener.document.joinForm.id.value = id;
        opener.document.joinForm.isIdCheck.value=true;
        console.log($(opener.document).find("#id").val());
        window.close();
    });
////////////////////////////////////////////////////////////////////////////

///////////////////아이디 중복확인 버튼이 클릭되면 유효성 검사하는 함수/////////////
    $("#idCheckForm").on("submit", function() {
        let id = $("#checkId").val();

        if(id.length == 0) {
            alert("아이디를 입력해주세요");
            return false;
        }
        if(id.length < 5) {
            alert("아이디는 5자 이상 입력해주세요");
            return false;
        }
    });
////////////////////////////////////////////////////////////////////////////

    // 소셜 로그인 버튼 클릭 이벤트 추가
    $(".btn-outline-primary, .btn-outline-warning, .btn-outline-info").on("click", function () {
        console.log("Social login clicked: " + $(this).text());
    });
}); // end $(function() {}) DOM이 준비되면 끝


///////유효성 검사////////////////////////////////////////
function joinFormCheck(idJoinForm) {

    let name = $("#name").val();
    let id = $("#id").val();
    let pass1 = $("#pass1").val();
    let pass2 = $("#pass2").val();
    let zipcode = $("#zipcode").val();
    let address1 = $("#address1").val();
    let emailId = $("#emailId").val();
    let emailDomain = $("#emailDomain").val();
    let mobile2 = $("#mobile2").val();
    let mobile3 = $("#mobile3").val();
    let isIdCheck = $("#isIdCheck").val();

    if(name.length == 0) {
        alert("이름을 입력해주세요");
        return false;
    }
    if(id.length == 0) {
        alert("아이디를 입력해주세요");
        return false;
    }
    if(pass1.length == 0) {
        alert("비밀번호를 입력해주세요");
        return false;
    }
    if(pass2.length == 0) {
        alert("비밀번호 확인을 입력해주세요");
        return false;
    }
    if(pass1 != pass2) {
        alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        return false;
    }
    if(zipcode.length == 0) {
        alert("우편번호를 입력해주세요");
        return false;
    }
    if(address1.length == 0) {
        alert("주소를 입력해주세요");
        return false;
    }
    if(emailId.length == 0) {
        alert("이메일 아이디를 입력해주세요");
        return false;
    }
    if(emailDomain.length == 0) {
        alert("이메일 도메인을 입력해주세요");
        return false;
    }
    if(mobile2.length == 0 || mobile3.length == 0) {
        alert("휴태폰 번호를 입력해주세요");
        return false;
    }
}
///////////////////////////////////////////

//////////우편번호 찾기//////////////////////////////
function findZipcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            addr = data.roadAddress;

            if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                extraAddr += data.bname;
            }

            if(data.buildingName !== '' && data.apartment === 'Y'){
                extraAddr += (extraAddr !== '' ?
                    ', ' + data.buildingName : data.buildingName);
            }

            if(extraAddr !== ''){
                extraAddr = ' (' + extraAddr + ')';
            }

            // 상세주소까지 조합된 주소 정보
            addr += extraAddr;

            // 우편번호는 우편번호 입력란에
            $("#zipcode").val(data.zonecode);

            // 주소는 주소 입력란에 입력
            $("#address1").val(addr);
            $("#address2").focus();
        }
    }).open();
}
///////////////////////////////////////////////

//////////영문 대소문자, 숫자만 입력되었는지 체크/////////////////////////
function inputCharReplace() {
    let regExp = /[^a-zA-Z0-9]/gi;
    if(regExp.test($(this).val())) {
        alert("영문자와 숫자만 입력할 수 있습니다.");
        $(this).val($(this).val().replace(regExp, ""))
    }
}
///////이메일 도메인 입력 폼 컨트롤에서 영문,숫자 입력되었는지 체크/////////////////
function inputEmailDomainReplace() {
    let regExp = /[^a-zA-Z0-9\.]/gi;
    if(regExp.test($(this).val())) {
        alert("이메일 도메인은 영문자, 숫자, 점(.)만 입력할 수 있습니다.");
        $(this).val($(this).val().replace(regExp, ""))
    }
}
////////////////////////////////////////////////////////////////
