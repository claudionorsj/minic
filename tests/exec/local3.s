	.text
	.globl main
main:
	subq $8, %rsp
	movq $65, %rdi
	movq %rdi, 0(%rsp)
	movq 0(%rsp), %rdi
	call putchar
	movq 0(%rsp), %rdi
	addq $1, %rdi
	movq %rdi, 0(%rsp)
	call putchar
	movq 0(%rsp), %rdi
	addq $1, %rdi
	call putchar
	movq $10, %rdi
	call putchar
	movq $0, %rax
	addq $8, %rsp
	ret
	.data
