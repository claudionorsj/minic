	.text
	.globl main
main:
	movq $1, %rcx
	movq $0, %rdi
	subq %rcx, %rdi
	addq $66, %rdi
	call putchar
	movq $1, %rdi
	movq $0, %rcx
	subq %rdi, %rcx
	movq $0, %rdi
	subq %rcx, %rdi
	addq $65, %rdi
	call putchar
	movq $10, %rdi
	call putchar
	movq $0, %rax
	ret
	.data
