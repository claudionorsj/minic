	.text
	.globl main
main:
	subq $16, %rsp
	movq $65, %rdi
	movq %rdi, 0(%rsp)
	movq 0(%rsp), %rdi
	call putchar
	movq 0(%rsp), %rdi
	addq $1, %rdi
	movq %rdi, 0(%rsp)
	movq 0(%rsp), %rdi
	call putchar
	movq 0(%rsp), %rdi
	addq $1, %rdi
	movq %rdi, 8(%rsp)
	movq 0(%rsp), %rdi
	call putchar
	movq 8(%rsp), %rdi
	call putchar
	movq $10, %rdi
	call putchar
	movq $0, %rax
	addq $16, %rsp
	ret
	.data
