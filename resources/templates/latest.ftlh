<!DOCTYPE html>
<html>
<head>
  <title>Latest</title>
  <link rel="stylesheet"
         target="_blank" target="_blank" target="_blank" target="_blank" target="_blank" href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
  <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
  <script>hljs.initHighlightingOnLoad();</script>
</head>
<body>
<#list snippets as snippet>
<span id="load_date">${snippet.date}</span>
<pre id="code_snippet"><code>${snippet.code}</code></pre>
</#list>
</body>
</html>