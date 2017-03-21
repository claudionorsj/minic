	.text
	.globl main
add:
	movq %rdi, %rax
	addq %rsi, %rax
	ret
sub:
	subq %rsi, %rdi
	movq %rdi, %rax
	ret
mul:
	imulq %rsi, %rdi
	movq $8192, %rax
	movq $2, %rcx
	movq $0, %rdx
	idivq %rcx
	addq %rax, %rdi
	movq $8192, %rcx
	movq %rdi, %rax
	movq $0, %rdx
	idivq %rcx
	movq %rax, %rdi
	movq %rdi, %rax
	ret
div:
	movq $8192, %rcx
	imulq %rcx, %rdi
	movq %rsi, %rax
	movq $2, %rcx
	movq $0, %rdx
	idivq %rcx
	addq %rax, %rdi
	movq %rdi, %rax
	movq $0, %rdx
	idivq %rsi
	movq %rax, %rdi
	movq %rdi, %rax
	ret
of_int:
	movq %rdi, %rax
	movq $8192, %rdi
	imulq %rdi, %rax
	ret
iter:
	subq $80, %rsp
	movq %rdi, 0(%rsp)
	movq %rsi, 8(%rsp)
	movq %rdx, 16(%rsp)
	movq %rcx, 32(%rsp)
	movq %r8, 40(%rsp)
	movq 0(%rsp), %rdi
	subq $100, %rdi
	cmpq $0, %rdi
	je L71
	movq 32(%rsp), %rdi
	movq 32(%rsp), %rsi
	call mul
	movq %rax, 56(%rsp)
	movq 40(%rsp), %rdi
	movq 40(%rsp), %rsi
	call mul
	movq %rax, 48(%rsp)
	movq 56(%rsp), %rdi
	movq 48(%rsp), %rsi
	call add
	movq %rax, 24(%rsp)
	movq $4, %rdi
	call of_int
	movq 24(%rsp), %rbp
	cmpq %rbp, %rax
	jl L56
	addq $1, 0(%rsp)
	movq 56(%rsp), %rdi
	movq 48(%rsp), %rsi
	call sub
	movq %rax, %rdi
	movq 8(%rsp), %rsi
	call add
	movq %rax, 64(%rsp)
	movq $2, %rdi
	call of_int
	movq %rax, 72(%rsp)
	movq 32(%rsp), %rdi
	movq 40(%rsp), %rsi
	call mul
	movq %rax, %rsi
	movq 72(%rsp), %rdi
	call mul
	movq %rax, %rdi
	movq 16(%rsp), %rsi
	call add
	movq 0(%rsp), %rdi
	movq 8(%rsp), %rsi
	movq 16(%rsp), %rdx
	movq 64(%rsp), %rcx
	movq %rax, %r8
	call iter
L37:
	addq $80, %rsp
	ret
L56:
	movq $0, %rax
	jmp L37
L71:
	movq $1, %rax
	jmp L37
inside:
	subq $32, %rsp
	movq $0, 0(%rsp)
	movq %rdi, 8(%rsp)
	movq %rsi, 16(%rsp)
	movq $0, %rdi
	call of_int
	movq %rax, 24(%rsp)
	movq $0, %rdi
	call of_int
	movq 0(%rsp), %rdi
	movq 8(%rsp), %rsi
	movq 16(%rsp), %rdx
	movq 24(%rsp), %rcx
	movq %rax, %r8
	call iter
	addq $32, %rsp
	ret
run:
	subq $88, %rsp
	movq %rdi, 16(%rsp)
	movq $2, %rcx
	movq $0, %rdi
	subq %rcx, %rdi
	call of_int
	movq %rax, 24(%rsp)
	movq $1, %rdi
	call of_int
	movq %rax, %rdi
	movq 24(%rsp), %rsi
	call sub
	movq %rax, 48(%rsp)
	movq $2, %rdi
	movq 16(%rsp), %rcx
	imulq %rcx, %rdi
	call of_int
	movq %rax, %rsi
	movq 48(%rsp), %rdi
	call div
	movq %rax, 32(%rsp)
	movq $1, %rcx
	movq $0, %rdi
	subq %rcx, %rdi
	call of_int
	movq %rax, 0(%rsp)
	movq $1, %rdi
	call of_int
	movq %rax, %rdi
	movq 0(%rsp), %rsi
	call sub
	movq %rax, 8(%rsp)
	movq 16(%rsp), %rdi
	call of_int
	movq %rax, %rsi
	movq 8(%rsp), %rdi
	call div
	movq %rax, 40(%rsp)
	movq $0, %rdi
	movq %rdi, 56(%rsp)
L127:
	movq 56(%rsp), %rcx
	movq 16(%rsp), %rdi
	cmpq %rdi, %rcx
	jl L124
	movq 80(%rsp), %rax
	addq $88, %rsp
	ret
L124:
	movq 56(%rsp), %rdi
	call of_int
	movq %rax, %rdi
	movq 40(%rsp), %rsi
	call mul
	movq %rax, %rsi
	movq 0(%rsp), %rdi
	call add
	movq %rax, 64(%rsp)
	movq 72(%rsp), %rdi
	movq $0, %rdi
	movq %rdi, 72(%rsp)
L114:
	movq 72(%rsp), %rcx
	movq $2, %rdi
	movq 16(%rsp), %rsi
	imulq %rsi, %rdi
	cmpq %rdi, %rcx
	jl L109
	movq $10, %rdi
	call putchar
	movq 56(%rsp), %rdi
	addq $1, %rdi
	movq %rdi, 56(%rsp)
	jmp L127
L109:
	movq 72(%rsp), %rdi
	call of_int
	movq %rax, %rdi
	movq 32(%rsp), %rsi
	call mul
	movq %rax, %rsi
	movq 24(%rsp), %rdi
	call add
	movq %rax, %rdi
	movq 64(%rsp), %rsi
	call inside
	cmpq $0, %rax
	je L98
	movq $48, %rdi
	call putchar
L94:
	movq 72(%rsp), %rdi
	addq $1, %rdi
	movq %rdi, 72(%rsp)
	jmp L114
L98:
	movq $49, %rdi
	call putchar
	jmp L94
main:
	movq $30, %rdi
	call run
	movq $0, %rax
	ret
	.data
