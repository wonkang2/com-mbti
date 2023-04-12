let signupButton = document.getElementById('saveButton');

signupButton.addEventListener('click', function () {
    const memberId = signupButton.getAttribute("data-member-id")
    let password1 = document.getElementById('password1');
    let password2 = document.getElementById('password2');
    let mbti = document.getElementById('mbti');

    if(!validate(password1, password2, mbti)) {
        return false;
    };
    console.log("입력값 검증 완료");
    const member = {
        password: password1.value,
        mbtiType: mbti.value
    }
    const url = "/api/members/" + memberId;
    fetch(url, {
        method: "PATCH",
        body: JSON.stringify(member),
        headers: {
            "Content-Type": "application/json"
        }
    }).then(res => {
        console.log(res);
        if (res.ok) {
            console.log("response URL: ", res.url);
            alert("수정되었습니다.")
            location.reload();
        } else {
            alert("서버에 문제가 발생하였습니다. 잠시후 시도해주세요.");
        }
    })
})

/**
 * 입력값을 체크하는 메서드
 * @param email
 * @param username
 * @param password1
 * @param password2
 * @param mbti
 * @returns {boolean}
 */
function validate(password1, password2, mbti) {
    if (password1.value !== "") {
        // 비밀번호 정규화: 최소 8자 최대 16자, 문자/숫자/특수문자 최소 1개 이상,
        let passwordRegulation = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,16}$/;

        if (password1.value != password2.value) {
            alert("입력하신 비밀번호가 일치하지 않습니다.");
            password1.focus();
            return false;
        }
        if (!check(passwordRegulation, password1, "비밀번호는 영문과 숫자, 특수문자 조합으로 최소8자 최대16자까지 가능합니다.")) {
            return false;
        };

    }
    if (mbti.value === 'none') {
        alert("MBTI를 선택해주세요.");
        mbti.focus();
        return false;
    }
    return true;
}

// /**
//  * test() 메서드를 통해 해당 값이 주어진 정규표현식에 만족하는지 검증하는 메서드
//  * @param re: 정규표현식
//  * @param what: 비교대상
//  * @param message: 에러메세지
//  * @returns {boolean}
//  */
function check(re, what, message) {
    if (re.test(what.value)) {
        return true;
    }
    alert(message);
    what.focus();
    return false;
}