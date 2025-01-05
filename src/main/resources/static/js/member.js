// DOM이 준비되면
$(function() {

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
        // location.href = "passCheck?" + data;
        // XMLHttpRequest, ES6 - fetch API - 비동기 처리 Promise - Axios
        // jQuery
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

    // 회원가입 폼이 전송될 때 유효성 검사
    $("#joinForm").submit(function() {
        return joinFormCheck();
    });

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


    $("#btnZipcode").click(findZipcode);


    $("#btnIdCheckClose").on("click", function() {
        let id = $(this).attr("data-id-value");
        opener.document.joinForm.id.value = id;
        opener.document.joinForm.isIdCheck.value=true;
        console.log($(opener.document).find("#id").val());
        window.close();
    });

    // 새 창에서 폼이 전송될 때 유효성 검사
    $("#idCheckForm").on("submit", function() {
        let id = $("#checkId").val();

        if(id.length == 0) {
            alert("아이디를 입력해주삼");
            return false;
        }
        if(id.length < 5) {
            alert("아이디는 5자 이상 입력해주삼");
            return false;
        }
    });


    $("#btnOverlapId").on("click", function() {
        let id = $("#id").val();
        url = "overlapIdCheck?id=" + id;

        if(id.length == 0) {
            alert("아이디를 입력해주삼");
            return false;
        }
        if(id.length < 5) {
            alert("아이디는 5자 이상 입력해주삼");
            return false;
        }

        window.open(url, "idCheck", "toolbar=no, scrollbars=no, resizeable=no, "
            + "status=no, menubar=no, width=500, height=330");
    });

    // 아이디 입력란에서 키보드 키가 눌렀다가 떨어질때 keyup
    //document.querySelector("#id").addEventListener("keyup", () => {});
    $("#id").on("keyup", function() {

        let regExp = /[^a-zA-Z0-9]/gi;
        if(regExp.test($("#id").val())) {
            alert("영문자와 숫자만 입력할 수 있음");
            $(this).val($(this).val().replace(regExp, ""))
        }
    });

    $("#pass1").on("keyup", inputCharReplace);
    $("#pass2").on("keyup", inputCharReplace);
    $("#emailId").on("keyup", inputCharReplace);
    $("#emailDomain").on("keyup", inputEmailDomainReplace);

    // 모달 로그인 폼 유효성 검사
    $("#modalLoginForm").submit(function() {

        let id = $("#modalUserId").val();
        let pass = $("#modalUserPass").val();

        if(id.length <= 0) {
            alert("아이디를 입력해주세요");
            $("#modalUserId").focus();
            return false;
        }
        if(pass.length <= 0) {
            alert("비밀번호를 입력해주세요");
            $("#modalUserPass").focus();
            return false;
        }
    });

    // 로그인 폼 유효성 검사
    $("#loginForm").submit(function() {

        let id = $("#userId").val();
        let pass = $("#userPass").val();

        if(id.length <= 0) {
            alert("아이디를 입력해주세요");
            $("#userId").focus();
            return false;
        }
        if(pass.length <= 0) {
            alert("비밀번호를 입력해주세요");
            $("#userPass").focus();
            return false;
        }
    });
}); // end $(function() {}) DOM이 준비되면 끝


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
        alert("이름을 입력해주삼~");
        return false;
    }
    if(id.length == 0) {
        alert("아이디를 입력해주삼~");
        return false;
    }
    if(pass1.length == 0) {
        alert("비밀번호를 입력해주삼~");
        return false;
    }
    if(pass2.length == 0) {
        alert("비밀번호 확인을 입력해주삼~");
        return false;
    }
    if(pass1 != pass2) {
        alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        return false;
    }
    if(zipcode.length == 0) {
        alert("우편번호를 입력해주삼~");
        return false;
    }
    if(address1.length == 0) {
        alert("주소를 입력해주삼~");
        return false;
    }
    if(emailId.length == 0) {
        alert("이메일 아이디를 입력해주삼~");
        return false;
    }
    if(emailDomain.length == 0) {
        alert("이메일 도메인을 입력해주삼~");
        return false;
    }
    if(mobile2.length == 0 || mobile3.length == 0) {
        alert("휴태폰 번호를 입력해주삼~");
        return false;
    }
}


function findZipcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            addr = data.roadAddress;

            // 법정동명이 있을 경우 추가한다. (법정리는 제외)
            // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
            if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                extraAddr += data.bname;
            }
            // 건물명이 있고, 공동주택일 경우 추가한다.
            if(data.buildingName !== '' && data.apartment === 'Y'){
                extraAddr += (extraAddr !== '' ?
                    ', ' + data.buildingName : data.buildingName);
            }
            // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
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

function inputCharReplace() {
    let regExp = /[^a-zA-Z0-9]/gi;
    if(regExp.test($(this).val())) {
        alert("영문자와 숫자만 입력할 수 있음");
        $(this).val($(this).val().replace(regExp, ""))
    }
}

function inputEmailDomainReplace() {
    let regExp = /[^a-zA-Z0-9\.]/gi;
    if(regExp.test($(this).val())) {
        alert("이메일 도메인은 영문자, 숫자, 점(.)만 입력할 수 있음");
        $(this).val($(this).val().replace(regExp, ""))
    }
}




