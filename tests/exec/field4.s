	.text
	.globl main
main:
	subq $8, %rsp
	movq $16, %rdi
	call sbrk
	movq %rax, 0(%rsp)
	movq $65, %rcx
	movq 0(%rsp), %rdi
	movq %rcx, 0(%rdi)
	movq 0(%rsp), %rdi
	movq 0(%rdi), %rdi
	call putchar
	movq $66, %rcx
	movq 0(%rsp), %rdi
	movq %rcx, 8(%rdi)
	movq 0(%rsp), %rdi
	movq 8(%rdi), %rdi
	call putchar
	movq $10, %rdi
	call putchar
	movq $0, %rax
	addq $8, %rsp
	ret
	.data
