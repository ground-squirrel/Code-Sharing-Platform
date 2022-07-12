<html>
<head>
    <title>Create</title>
    <script type="application/javascript">function send() {
    let object = {
        "code": document.getElementById("code_snippet").value
        "time": document.getElementById("time_restriction").value
        "views": document.getElementById("views_restriction").value
    };

    let json = JSON.stringify(object);

    let xhr = new XMLHttpRequest();
    xhr.open("POST", '/api/code/new', false)
    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
    xhr.send(json);

    if (xhr.status == 200) {
      alert("Success!");
    }
}</script>
</head>
<body>
<textarea id="code_snippet"> ... </textarea><br/>
<p>Time restriction:</p><input id="time_restriction" type="text"/><br/>
<p>Views restriction:</p><input id="views_restriction" type="text"/><br/>
<button id="send_snippet" type="submit" onclick="send()">Submit</button>
</body>
</html>