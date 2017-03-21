	.text
	.globl main
main:
	subq $8, %rsp
	movq $1, %rdi
	movq $0, %rcx
	movq %rdi, %rax
	cqto
	idivq %rcx
	movq %rax, %rdi
	call putchar
	movq 0(%rsp), %rax
	addq $8, %rsp
	ret
	.data
