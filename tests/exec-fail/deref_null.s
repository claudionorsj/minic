	.text
	.globl main
main:
	subq $8, %rsp
	movq $0, %rdi
	movq 0(%rdi), %rdi
	call putchar
	movq 0(%rsp), %rax
	addq $8, %rsp
	ret
	.data
