// 이메일 인증 후 서버로부터 받은 인증번호를 저장할 변수
let authNumber;
// 아이디 중복확인을 하였는가?
let checkedUsername = false;
// 이메일 인증을 하였는가?
let checkedEmail = false;

const checkEmailButton = document.getElementById("checkEmailButton");
checkEmailButton.addEventListener("click", function () {
    const authNumberWrapper = document.getElementById("authNumberWrapper");
    const emailInp = document.getElementById("email");
    const email = emailInp.value;

    if (!verifyEmail(emailInp)) {
        return;
    }

    const url = "/api/auth/check/emails?email=" + email;
    fetch(url)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else if (response.status == 409) {
                alert("이미 가입된 이메일입니다.");
            }
        })
        .then(data => {
            console.log("json - ", data);
            authNumber = data.authNumber;
            console.log("authNumber: ", authNumber);
            authNumberWrapper.style.display = "block";
            alert("인증번호가 전송되었습니다.");
        })
});
const authNumCheckButton = document.getElementById("authNumberCheckButton");
authNumCheckButton.addEventListener("click", function () {
    console.log("인증 버튼 호출");
    console.log("authNumber:", authNumber);
    const authNumberInput = document.getElementById("authNumberInput");
    if (authNumber == authNumberInput.value) {
        checkedEmail = true;
        document.getElementById("email").disabled = true;
        document.getElementById("checkEmailButton").disabled = true;
        document.getElementById("authNumberInput").disabled = true;
        document.getElementById("authNumberCheckButton").disabled = true;
        alert("인증에 성공하였습니다.");
    } else {
        alert("인증번호가 일치하지 않습니다. \n다시 확인해주세요.");
    }
});

const checkUsernameButton = document.getElementById("checkUsernameButton");
checkUsernameButton.addEventListener("click", function () {
    const usernameInp = document.getElementById("username");
    const username = usernameInp.value;

    if (!verifyUsername(usernameInp)) {
        return;
    }

    const url = "/api/auth/check/usernames?username=" + username;
    fetch(url)
        .then(response => {
                if (response.status == 200) {
                    usernameInp.disabled = true;
                    checkedUsername = true;
                    const modifyButton = document.createElement("button");
                    modifyButton.innerText = "수정하기";
                    modifyButton.setAttribute("id", "modify__username");
                    modifyButton.setAttribute("class", checkUsernameButton.className);
                    modifyButton.onclick = modifyUsername;
                    checkUsernameButton.replaceWith(modifyButton);
                    alert("사용할 수 있는 ID입니다.");
                } else if (response.status == 409) {
                    alert("이미 존재하는 ID입니다.");
                } else {
                    alert("서버 문제로 요청에 실패하였습니다.");
                }
            }
        )
});

function modifyUsername() {
    console.log("중복확인 해제 이벤트 호출");
    const modifyUsernameButton = document.getElementById("modify__username");
    const usernameInp = document.getElementById("username");

    usernameInp.disabled = false;
    checkedUsername = false;
    modifyUsernameButton.replaceWith(checkUsernameButton);
};

// 회원가입
let signupButton = document.getElementById('signupButton');
signupButton.addEventListener('click', function () {
    let email = document.getElementById('email');
    let username = document.getElementById('username');
    let password1 = document.getElementById('password1');
    let password2 = document.getElementById('password2');
    let mbti = document.getElementById('mbti');

    if (!validate(email, username, password1, password2, mbti)) {
        return false;
    }

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
function validate(emailInp, usernameInp, passwordInp1, passwordInp2, mbtiInp) {
    if (!checkedEmail) {
        alert("이메일 인증을 완료해주세요.");
        emailInp.focus();
        return false;
    }

    if (!checkedUsername) {
        alert("아이디 중복검사를 완료해주세요.");
        usernameInp.focus();
        return false;
    }

    if ((passwordInp1.value) === "") {
        alert("비밀번호를 입력해주세요.");
        passwordInp1.focus();
        return false;
    }

    if ((passwordInp2.value) === "") {
        alert("비밀번호를 입력해주세요.");
        passwordInp2.focus();
        return false;
    }

    console.log("mbti: ", mbti.value);
    if (mbtiInp.value === 'none') {
        alert("MBTI를 선택해주세요.");
        mbtiInp.focus();
        return false;
    }

    if (passwordInp1.value != passwordInp2.value) {
        alert("입력하신 비밀번호가 일치하지 않습니다.");
        passwordInp1.focus();
        return false;
    }

    /**
     * 유효성 검증
     */
    // 비밀번호 정규화: 최소 8자 최대 16자, 문자/숫자/특수문자 최소 1개 이상,
    let passwordRegulation = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,16}$/;

    if (!check(passwordRegulation, password1, "비밀번호는 영문과 숫자, 특수문자 조합으로 최소8자 최대16자까지 가능합니다.")) {
        return false;
    }

    return true;
}

function verifyEmail(emailInp) {
    if ((emailInp.value) === "") {
        alert("이메일을 입력해주세요.");
        emailInp.focus();
        return false;
    }
    // 이메일 정규화 공식
    let emailRegulation = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/;

    if (!check(emailRegulation, emailInp, "이메일을 다시 확인해주세요.")) {
        return false;
    }
    return true;
}

function verifyUsername(usernameInp) {
    if ((usernameInp.value) === "") {
        alert("아이디를 입력해주세요.");
        usernameInp.focus();
        return false;
    }
    // 아이디 정규화 공식
    let usernameRegulation = /^[a-zA-Z0-9]{6,12}$/;

    if (!check(usernameRegulation, usernameInp, "아이디는 영문과 숫자로 6~12자까지 입력가능합니다.")) {
        return false;
    }
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