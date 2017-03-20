	.text
	.globl main
main:
	movq $16, %rdi
	call sbrk
	movq %rax, s
	movq $65, %rcx
	movq s, %rdi
	movq %rcx, 0(%rdi)
	movq s, %rdi
	movq 0(%rdi), %rdi
	call putchar
	movq $66, %rcx
	movq s, %rdi
	movq %rcx, 8(%rdi)
	movq s, %rdi
	movq 8(%rdi), %rdi
	call putchar
	movq $10, %rdi
	call putchar
	movq $0, %rax
	ret
	.data
s:
	.quad 0
