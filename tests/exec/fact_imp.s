	.text
	.globl main
fact_imp:
	movq $1, %rax
L12:
	cmpq $1, %rdi
	jg L10
	ret
L10:
	movq %rdi, %rcx
	subq $1, %rcx
	movq %rcx, %rdi
	addq $1, %rcx
	imulq %rcx, %rax
	jmp L12
main:
	movq $0, %rdi
	call fact_imp
	subq $1, %rax
	cmpq $0, %rax
	je L32
L30:
	movq $1, %rdi
	call fact_imp
	subq $1, %rax
	cmpq $0, %rax
	je L26
L24:
	movq $5, %rdi
	call fact_imp
	subq $120, %rax
	cmpq $0, %rax
	je L20
L18:
	movq $10, %rdi
	call putchar
	movq $0, %rax
	ret
L20:
	movq $51, %rdi
	call putchar
	jmp L18
L26:
	movq $50, %rdi
	call putchar
	jmp L24
L32:
	movq $49, %rdi
	call putchar
	jmp L30
	.data
