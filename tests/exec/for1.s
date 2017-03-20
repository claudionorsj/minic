	.text
	.globl main
main:
	movq $0, %rdi
	movq $0, %rsi
L26:
	cmpq $10, %rsi
	jl L24
	subq $100, %rdi
	cmpq $0, %rdi
	je L6
L4:
	movq $10, %rdi
	call putchar
	movq $0, %rax
	ret
L6:
	movq $33, %rdi
	call putchar
	jmp L4
L24:
	movq $10, %rcx
L22:
	cmpq $0, %rcx
	jg L20
	addq $1, %rsi
	jmp L26
L20:
	addq $1, %rdi
	subq $1, %rcx
	jmp L22
	.data
