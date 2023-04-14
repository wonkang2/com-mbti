let postButton = document.getElementById('postButton');
postButton.addEventListener('click', function () {
    console.log("PostButton 클릭 이벤트");
    let title = document.getElementById('title').value;
    let content = document.getElementById('content').value;
    let files = document.getElementById('files').files;
    console.log("title: ", title);
    console.log("content: ", content);
    console.log("files: ", files);
    if(!validate(title, content)) {
        return false;
    }
    console.log("입력값 검증 완료");

    let formData = new FormData();
    formData.append("title", title);
    formData.append("content", content);
    for (let i = 0; i < files.length; i++) {
        formData.append("files[" + i + "]", files[i]);
    }

    for (let key of formData.keys()) {
        console.debug(key, ":", formData.get(key));
    }

    const uri = "/api/bulletins";
    fetch(uri, {
        method: "POST",
        body: formData
    }).then(res => {
        console.log(res);
        if (res.status == 201) {
            console.log("response URL: ", res.url);
            window.location.replace(res.headers.get("Location"));
            alert("게시글이 정상적으로 등록되었습니다.")
        } else {
            alert("서버에 문제가 발생하였습니다. 잠시후 시도해주세요.");
        }
    })
})

function validate(title, content) {
    if (title == "") {
        alert("제목은 필수입니다.");
        titleInput.focus();
        return false;
    }

    if (content == "") {
        alert("내용은 필수입니다.");
        content.focus();
        return false;
    }

    return true;
}