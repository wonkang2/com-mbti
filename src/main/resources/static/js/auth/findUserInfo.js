const findUsername = document.getElementById("findUsername");
findUsername.addEventListener("click", function () {
    const passwordWrapper = document.getElementById("password__wrapper");
    const usernameInfo = document.getElementById("info__username");
    const funcButton = document.getElementById("signupButton");

    usernameInfo.innerText = "회원가입 시 입력한 이메일을 입력해주세요.";
    passwordWrapper.remove();

    funcButton.type = "button";
    funcButton.innerText = "조회하기";
    funcButton.id = "findButton";
    funcButton.onclick = findUsernameFunc;
});

const findPassword = document.getElementById("findPassword");
findPassword.addEventListener("click", function () {
    const usernameInfo = document.getElementById("info__username");
    const passwordInfo = document.getElementById("info__password");
    const funcButton = document.getElementById("signupButton");
    const usernameInput = document.getElementById("password1");
    usernameInput.type = "text";

    usernameInfo.innerText = "회원가입 시 입력한 이메일을 입력해주세요.";
    passwordInfo.innerText = "아이디를 입력해주세요.";

    funcButton.type = "button";
    funcButton.innerText = "조회하기";
    funcButton.id = "findButton";
    funcButton.onclick = findPasswordFunc;
});
function findPasswordFunc() {
    console.log("조회하기 버튼 이벤트 호출");
    const email = document.getElementById("username").value;
    const username = document.getElementById("password1").value;

    // 이메일 정규화 공식
    let emailRegulation = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/;
    // 아이디 정규화 공식
    let usernameRegulation = /^[a-zA-Z0-9]{6,12}$/;
    if (email === "" || !check(emailRegulation, email)) {
        alert("이메일을 다시 확인해주세요.");
        return false;
    }
    if (username === "" || !check(usernameRegulation, username)) {
        alert("아이디를 다시 확인해주세요.");
        return false;
    }

    const url = "/api/members/recovery?email=" + email +"&username=" + username
    fetch(url)
        .then(response => {
                if (response.ok) {
                    alert("입력하신 이메일로 임시비밀번호가 발급되었습니다.");
                    console.log(response.json());
                    location.reload();
                } else {
                    alert("이메일 또는 아이디를 다시 확인해주세요.");
                }
            }
        );
}
function findUsernameFunc() {
    console.log("조회하기 버튼 이벤트 호출")
    const email = document.getElementById("username").value;

    // 이메일 정규화 공식
    let emailRegulation = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/;
    if (email === "" || !check(emailRegulation, email)) {
        alert("이메일을 다시 확인해주세요.");
        return false;
    }

    const url = "/api/members?email=" + email
    let username;
    fetch(url)
        .then(response => {
            if (response.ok) {
                return response.json()
            } else {
                alert("이메일을 다시 확인해주세요.");
            }
        }).then(data => {
            username = data["username"];
            username = encodeUsername(username);

            alert("등록된 아이디는 " + username + "입니다.");
            location.reload();
        }
    )
};

// 찾은 아이디를 일부 숨기기 위해 작성한 메서드
function encodeUsername(username) {
    let usernameArr = [...username];
    let len = usernameArr.length - 1;
    // 아이디 끝부터 3자리를 숨김처리 -> 아이디의 길이는 최소 6 ~ 12자이니 최소의 반만큼..
    for (let i = 0; i < 3; i++) {
        usernameArr[len - i] = "*";
    }
    username = usernameArr.join("");
    return username;
}

function check(re, what) {
    if (re.test(what)) {
        return true;
    }
    return false;
}