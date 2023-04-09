const bulletinId = document.getElementById("bulletin").getAttribute("data-bulletin-id");
// *********************** 게시글 관련 ***********************
/**
 * 게시글 수정 버튼 이벤트
 */
const bulletinEditButton = document.getElementById('bulletinEditButton');
bulletinEditButton.addEventListener("click", function () {
    console.log("게시글 수정 버튼 클릭 이벤트 호출")
    location.href = "/bulletin-board/bulletins/" + bulletinId + "/edit";
});

/**
 * 게시글 삭제 버튼 이벤트
 */
const bulletinDeleteButton = document.getElementById('bulletinDeleteButton');
bulletinDeleteButton.addEventListener("click", function () {
    console.log("게시글 삭제 버튼 클릭 이벤트 호출");
    if (confirm("정말 삭제하시겠습니까?")) {

        const uri = "/api/bulletins/" + bulletinId;
        fetch(uri, {
            method: "DELETE"
        }).then(response => {
            if (response.ok) {
                alert("삭제되었습니다.");
                location.replace(response.headers.get("Location"))
            } else {
                throw new Error("요청에 실패하였습니다. 잠시 후 다시 시도해주세요.");
            }
        }).catch(error => {
            alert(error);
        })
    }
});

// *********************** 댓글 관련 ***********************
/**
 * 댓글 등록 버튼 이벤트
 */
const commentPostButton = document.getElementById("commentPostButton");
commentPostButton.addEventListener("click", function () {
    console.log("댓글 등록 버튼 클릭 이벤트 호출");
    const commentContent = document.getElementById("commentContent").value;
    console.log(commentContent);

    const comment = {
        content: commentContent
    }

    const uri = "/api/bulletins/" + bulletinId + "/comments";
    fetch(uri, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(comment),
    }).then(response => {
        if (response.status === 201) {
            alert("등록되었습니다.");
            location.reload();
        } else {
            throw new Error("요청에 실패하였습니다.");
        }
    }).catch(error => alert(error));
});

/**
 * 댓글 수정 버튼 이벤트
 */
const commentEditButtons = document.querySelectorAll('.comment__edit__button');
commentEditButtons.forEach(function (commentEditButton) {
    commentEditButton.addEventListener('click', function (event) {
        console.log("댓글 수정 버튼 클릭 이벤트 호출")

        let target = event.target;
        console.log("deleteBtn: ", target)
        const commentId = target.getAttribute("data-comment-id");
        console.log("commentId: ", commentId);

        let oldContent = document.getElementById('content-' + commentId);
        console.log("oldContent: ", oldContent);
        let newContent = document.createElement('input');
        newContent.className = 'input__detail';
        newContent.value = oldContent.innerHTML;
        newContent.id = oldContent.id;
        console.log("oldContent: ", oldContent);
        console.log("newContent: ", newContent);

        document.getElementById('editButton-'+commentId).style.display = 'none';
        document.getElementById('deleteButton-' + commentId).style.display = 'none';
        let saveButton = document.getElementById('saveButton-' + commentId);
        saveButton.style.display = 'block'

        oldContent.replaceWith(newContent);
    })
})

/**
 * 댓글 저장 버튼 이벤트
 */
// 댓글 수정 이벤트
const commentSaveButtons = document.querySelectorAll('.comment__save__button');
commentSaveButtons.forEach(function (commentSaveButton) {
    commentSaveButton.addEventListener('click', function (event) {
        console.log("댓글 저장 이벤트 호출");
        let target = event.target;
        const commentId = target.getAttribute("data-comment-id");
        let content = document.getElementById('content-' + commentId);
        const comment = {
            content: content.value
        }
        console.log("comment: ",comment)

        const url = "/api/comments/" + commentId;
        fetch(url, {
            method: "PATCH",
            body: JSON.stringify(comment),
            headers: {
                "Content-Type": "application/json",
            }
        }).then(res => {
            let result;
            if(res.ok) {
                result = "댓글이 수정되었습니다.";
            } else {
                result = "댓글 수정을 실패하였습니다.";
            }
            alert(result);
            window.location.reload();
        })

    });
})

/**
 * 댓글 삭제 버튼 이벤트
 */
const commentDeleteButtons = document.querySelectorAll('.comment__delete__button');
commentDeleteButtons.forEach(function (commentDeleteButton) {
    commentDeleteButton.addEventListener('click', function (event) {
        console.log("댓글 삭제 버튼 클릭 이벤트 호출")
        if (confirm("정말로 삭제하시겠습니까?")) {
            let target = event.target;
            console.log("deleteBtn: ", target)
            const commentId = target.getAttribute("data-comment-id");
            console.log("commentId: ", commentId);

            const url = "/api/comments/" + commentId;
            fetch(url, {
                method: "DELETE",
            }).then(response => {
                console.log(response);
                if (response.ok) {
                    alert("삭제되었습니다.");
                    location.reload();
                } else {
                    alert("요청이 실패하였습니다.");
                }
            });
        }
    })
});