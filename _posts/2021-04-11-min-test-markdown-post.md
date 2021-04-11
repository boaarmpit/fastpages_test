---
toc: true
layout: post
description: Another minimal example of using markdown with fastpages.
categories: [markdown]
title: Another Example Markdown Post
comments: true
---
# Example Markdown Post

Example code from [pandas documentation](https://pandas.pydata.org/docs/reference/api/pandas.DataFrame.pipe.html):

Nesting functions:
```python
f(g(h(df), arg1=a), arg2=b, arg3=c)
```

Using pandas piping feature:
```python
(df.pipe(h)
   .pipe(g, arg1=a)
   .pipe(func, arg2=b, arg3=c)
)  
```
