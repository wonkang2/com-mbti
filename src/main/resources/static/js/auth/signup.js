let signupButton = document.getElementById('signupButton');

signupButton.addEventListener('click', function () {
    let email = document.getElementById('email');
    let username = document.getElementById('username');
    let password1 = document.getElementById('password1');
    let password2 = document.getElementById('password2');
    let mbti = document.getElementById('mbti');

    if(!validate(email, username, password1, password2, mbti)) {
        return false;
    };
    console.log("입력값 검증 완료");
    const member = {
        username: username.value,
        password: password1.value,
        email: email.value,
        mbti: mbti.value
    }
    const url = "/api/signup";
    fetch(url, {
        method: "POST",
        body: JSON.stringify(member),
        headers: {
            "Content-Type": "application/json"
        }
    }).then(res => {
        console.log(res);
        if (res.status == 201) {
            console.log("response URL: ", res.url);
            window.location.replace(res.headers.get("Location"));
            alert("회원가입이 정상적으로 이루어졌습니다.")
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
function validate(email, username, password1, password2, mbti) {
    /**
     * 필수값 및 비밀번호 일치 체크
     */
    if ((email.value) === "") {
        alert("이메일을 입력해주세요.");
        email.focus();
        return false;
    }

    if ((username.value) === "") {
        alert("아이디를 입력해주세요.");
        username.focus();
        return false;
    }

    if ((password1.value) === "") {
        alert("비밀번호를 입력해주세요.");
        password1.focus();
        return false;
    }

    if ((password2.value) === "") {
        alert("비밀번호를 입력해주세요.");
        password2.focus();
        return false;
    }

    console.log("mbti: ",mbti.value);
    if (mbti.value === 'none') {
        alert("MBTI를 선택해주세요.");
        mbti.focus();
        return false;
    }

    if (password1.value != password2.value) {
        alert("입력하신 비밀번호가 일치하지 않습니다.");
        password1.focus();
        return false;
    }

    /**
     * 유효성 검증
     */
    // 아이디 정규화 공식
    let usernameRegulation = /^[a-zA-Z0-9]{4,12}$/;

    // 비밀번호 정규화: 최소 8자 최대 16자, 문자/숫자/특수문자 최소 1개 이상,
    let passwordRegulation = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,16}$/;
    // 이메일 정규화 공식
    let emailRegulation = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/;

    if(!check(emailRegulation, email, "이메일을 다시 확인해주세요.")) {
        return false;
    };
    if (!check(usernameRegulation, username, "아이디는 영문과 숫자로 4~12자까지 입력가능합니다.")) {
        return false;
    };
    if (!check(passwordRegulation, password1, "비밀번호는 영문과 숫자, 특수문자 조합으로 최소8자 최대16자까지 가능합니다.")) {
        return false;
    };

    return true;
}

/**
 * test() 메서드를 통해 해당 값이 주어진 정규표현식에 만족하는지 검증하는 메서드
 * @param re: 정규표현식
 * @param what: 비교대상
 * @param message: 에러메세지
 * @returns {boolean}
 */
function check(re, what, message) {
    if (re.test(what.value)) {
        return true;
    }
    alert(message);
    what.focus();
    return false;
}